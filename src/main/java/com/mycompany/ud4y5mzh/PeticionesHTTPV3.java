/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.ud4y5mzh;

import java.io.File;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

public class PeticionesHTTPV3 {

    public int almacenarPagina(String esquema, String servidor, String recurso, String path)
            throws Exception {

        String direccion = esquema + servidor + recurso;

        System.out.println("Descargando " + direccion);
        HttpClient httpCliente = HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        HttpRequest httpPeticion = HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create(direccion))
                .headers("Content-Type", "text/plain")
                .setHeader("User-Agent", "Mozilla/5.0")
                .build();

        HttpResponse<Path> httpRespuesta = httpCliente.send(httpPeticion,
                HttpResponse.BodyHandlers.ofFile(
                        Path.of(path)));

        StringBuilder html = new StringBuilder();
        int caracter;
        File fichero = new File(path);
        FileReader fr = new FileReader(fichero);
        while ((caracter = fr.read()) != -1) {
            html.append((char) caracter);
        }

        String titulo = html.toString().split("<div class=\"poster\">\n"
                + "        <a data-id=")[1].split("alt=\"")[1].split("\">")[0];
        System.out.println("Título -> " + titulo);

        return httpRespuesta.statusCode();

    }

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Escriba el término: ");
            String termino = sc.nextLine();
            System.out.print("Indique la página: ");
            int page = sc.nextInt();
            sc.nextLine();
            System.out.println("Término que se va a buscar: " + termino);
            System.out.println("Página: " + page);

            String esquema = "https://";

            String servidor = "www.themoviedb.org/search/tv";
            String recurso = "?query=" + URLEncoder.encode(termino.toLowerCase() + "&page="+page, StandardCharsets.UTF_8.name());

            String path = "C:/Descargas/" + termino + ".html";

            PeticionesHTTPV3 peticion = new PeticionesHTTPV3();

            int codigoEstado = peticion.almacenarPagina(esquema, servidor, recurso, path);

            if (codigoEstado == HttpURLConnection.HTTP_OK) {
                System.out.println("Descarga finalizada.");
            } else {
                System.err.println("Error: " + codigoEstado);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
