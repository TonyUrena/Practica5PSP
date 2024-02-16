package com.dam.proyectospring;

import com.dam.proyectospring.model.Piloto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@SpringBootApplication
public class Main {
    // Crear un WebClient para realizar llamadas HTTP
    private static WebClient client = WebClient.create("http://localhost:8080");

    public static void main(String[] args) {
        // Iniciar la aplicación Spring Boot
        SpringApplication.run(Main.class, args);
        // Crear un lector de entrada para recibir datos del usuario
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        boolean salir = false;
        int opcion;

        // Menú de opciones para interactuar con la aplicación
        while (!salir) {
            System.out.println("\n");
            try {
                System.out.println("""
                        1 Mostrar a todos los pilotos.
                        2 Mostrar un piloto dado un id.
                        3 Crear un piloto con nuevos datos.
                        4 Actualizar un piloto dado un id concreto
                        5 Borrar un piloto dado un id.
                        0 Salir
                        """);
                // Leer la opción del usuario
                opcion = Integer.parseInt(reader.readLine());

                switch (opcion) {
                    case 1:
                        getAll(); // Mostrar todos los pilotos
                        break;

                    case 2:
                        System.out.print("Introduce el ID del piloto: ");
                        getOne(reader.readLine()); // Mostrar un piloto por ID
                        break;

                    case 3:
                        addOne(getPiloto(null)); // Añadir un nuevo piloto
                        break;

                    case 4:
                        System.out.println("Introduce el ID del piloto: ");
                        // Obtener un piloto por ID para verificar su existencia antes de actualizarlo
                        String id = reader.readLine();
                        Piloto pilot = client.get().uri("piloto/{id}", id).retrieve().bodyToMono(Piloto.class).block();

                        if (pilot != null) {
                            updateOne(getPiloto(id)); // Actualizar un piloto existente
                        } else {
                            System.err.println("El piloto no existe");
                        }
                        break;

                    case 5:
                        System.out.println("Introduce el ID del piloto: ");
                        deleteOne(reader.readLine()); // Borrar un piloto por ID
                        break;

                    case 0:
                        salir = true; // Salir del bucle y terminar la aplicación
                        break;
                }
            } catch (IOException ignored) {

            } catch (NumberFormatException exception) {
                System.err.println("Input mismatch"); // Manejar error de formato de entrada
            }
        }
    }

    // Método para obtener datos de un nuevo piloto desde la entrada del usuario
    private static Piloto getPiloto(String id) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String nuevoIdPiloto;

        if (id == null) {
            System.out.print("Id: ");
            nuevoIdPiloto = reader.readLine();
        } else {
            nuevoIdPiloto = id;
        }

        System.out.print("Nombre: ");
        String nuevoNombrePiloto = reader.readLine();

        System.out.print("Codigo: ");
        String nuevaAbreviaturaPiloto = reader.readLine();

        boolean reintento;
        int nuevoNumeroPiloto = 0;

        do {
            try {
                System.out.print("Número: ");
                nuevoNumeroPiloto = Integer.parseInt(reader.readLine());
                reintento = false;
            } catch (NumberFormatException e) {
                reintento = true;
                System.err.println("Formato de número incorrecto");
            }
        } while (reintento);

        System.out.print("Equipo: ");
        String nuevoEquipoPiloto = reader.readLine();

        System.out.print("País: ");
        String nuevoPaisPiloto = reader.readLine();

        LocalDate nuevaFechaNacimientoPiloto = null;

        do {
            try {
                System.out.print("Fecha de nacimiento (AAAA-MM-DD): ");

                nuevaFechaNacimientoPiloto = LocalDate.parse(reader.readLine());
                reintento = false;

            } catch (DateTimeParseException e) {
                reintento = true;
                System.err.println("Formato de fecha incorrecto");
            }
        } while (reintento);

        return new Piloto(nuevoIdPiloto, nuevoNombrePiloto, nuevaAbreviaturaPiloto,
                nuevoNumeroPiloto, nuevoEquipoPiloto, nuevoPaisPiloto, nuevaFechaNacimientoPiloto);
    }

    // Método para obtener y mostrar un piloto por ID
    private static void getOne(String id) {
        Piloto pilotoMono = client.get()
                .uri("piloto/{id}", "1")
                .retrieve()
                .bodyToMono(Piloto.class)
                .block();

        if (pilotoMono != null) {
            System.out.println(pilotoMono);
        } else {
            System.err.println("Piloto no encontrado");
        }
    }

    // Método para obtener y mostrar todos los pilotos
    private static void getAll() {
        List<Piloto> pilotoFlux = client.get()
                .uri("pilotos")
                .retrieve()
                .bodyToFlux(Piloto.class)
                .collectList()
                .block();

        if (pilotoFlux != null) {
            pilotoFlux.forEach(System.out::println);
        } else {
            System.err.println("Pilotos no encontrados");
        }
    }

    // Método para añadir un nuevo piloto
    private static void addOne(Piloto piloto) {
        Piloto pilotoMono = client.post()
                .uri("pilotos")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(piloto))
                .retrieve()
                .bodyToMono(Piloto.class)
                .block();

        if (pilotoMono != null) {
            System.out.println("Añadido: " + pilotoMono.getDriver());
        } else {
            System.err.println("No se pudo modificar el piloto");
        }
    }

    // Método para actualizar un piloto existente
    private static void updateOne(Piloto piloto) {
        Piloto pilotoMono = client.put()
                .uri("pilotos")
                .body(BodyInserters.fromValue(piloto))
                .retrieve()
                .bodyToMono(Piloto.class)
                .block();

        if (pilotoMono != null) {
            System.out.println("Modificado: " + pilotoMono.getDriver());
        } else {
            System.err.println("No se pudo modificar el piloto");
        }
    }

    // Método para eliminar un piloto por ID
    private static void deleteOne(String id) {
        Piloto pilotoEliminar = client.get().uri("piloto/", id).retrieve().bodyToMono(Piloto.class).block();
        if (pilotoEliminar != null) {
            client.delete()
                    .uri("pilotos", BodyInserters.fromValue(pilotoEliminar))
                    .retrieve();
            System.out.println("Piloto eliminado");
        } else {
            System.err.println("Piloto no encontrado");
        }
    }

}