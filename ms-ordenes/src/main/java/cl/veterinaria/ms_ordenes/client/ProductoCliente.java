package cl.veterinaria.ms_ordenes.client;

import cl.veterinaria.ms_ordenes.exception.OrdenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class ProductoCliente {

    private final RestTemplate restTemplate;
    private final String productosUrl;

    public ProductoCliente(RestTemplate restTemplate, @Value("${ms.productos.url}") String productosUrl) {
        this.restTemplate = restTemplate;
        this.productosUrl = productosUrl;
    }

    public ProductoCatalogo obtener(String codigo) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(productosUrl)
                    .pathSegment("{codigo}")
                    .buildAndExpand(codigo)
                    .toUri();
            ProductoCatalogo producto = restTemplate.getForObject(uri, ProductoCatalogo.class);
            if (producto == null) {
                throw new OrdenException(HttpStatus.BAD_GATEWAY, "El catálogo devolvió una respuesta vacía.");
            }
            return producto;
        } catch (HttpClientErrorException.NotFound exception) {
            throw new OrdenException(HttpStatus.NOT_FOUND, "No se encontró el producto " + codigo + ".");
        } catch (RestClientException exception) {
            throw new OrdenException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo consultar el catálogo.");
        }
    }

    public void descontarStock(String codigo, int cantidad) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(productosUrl)
                    .pathSegment("{codigo}", "descontar-stock")
                    .queryParam("cantidad", "{cantidad}")
                    .buildAndExpand(codigo, cantidad)
                    .toUri();
            restTemplate.patchForObject(uri, null, Object.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new OrdenException(HttpStatus.NOT_FOUND, "No se encontró el producto " + codigo + ".");
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new OrdenException(HttpStatus.CONFLICT, "El stock de " + codigo + " cambió; revisa el carrito.");
        } catch (RestClientException exception) {
            throw new OrdenException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo actualizar el stock del producto.");
        }
    }

    public record ProductoCatalogo(String codigo, String nombre, Integer stock, Integer precio) {
    }
}
