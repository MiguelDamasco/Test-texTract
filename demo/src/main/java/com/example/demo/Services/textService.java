package com.example.demo.Services;


import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.demo.DTO.TextDTO;
import com.example.demo.DTO.userDTO;

@Service
public class textService {
    
    @Autowired
    private RestTemplate restTemplate;

    public userDTO getText(String fileName) {
		 // Crear un objeto Map para los parámetros a enviar
        Map<String, String> parametros = new HashMap<>();
        parametros.put("bucket", "bucket-prueba-aws-forggstar");
        parametros.put("nombre", fileName);

        // Convertir el Map a JSON manualmente si es necesario (aunque RestTemplate lo hace automáticamente)
        // Crear los encabezados para indicar que se enviará un JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Crear la entidad HttpEntity con el cuerpo (parametros) y los encabezados
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parametros, headers);

        // URL de tu API Gateway que invoca la Lambda
        String url = "https://mrkbrtnhuf.execute-api.us-east-2.amazonaws.com/imagen";

        try {
            // Llamar a la API de Lambda usando postForObject
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                requestEntity, 
                String.class
            );
            
            // Obtener la respuesta del cuerpo de la respuesta
            String responseBody = response.getBody();
            System.out.println("Response: " + responseBody);

            // Procesar la respuesta de Lambda para obtener los datos que necesitas
            String[] resul = AlgoritmoConersion.obtenerDatos(convert(responseBody));

            // Crear el objeto userDTO con la información procesada
            userDTO user = crearUserDTO(resul);
            return user;

        } catch (Exception e) {
            System.err.println("Error al invocar la API de Lambda: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

	public TextDTO getTextPure(String fileName) {
		// Crear un objeto Map para los parámetros a enviar
	   Map<String, String> parametros = new HashMap<>();
	   parametros.put("bucket", "bucket-prueba-aws-forggstar");
	   parametros.put("nombre", fileName);

	   // Convertir el Map a JSON manualmente si es necesario (aunque RestTemplate lo hace automáticamente)
	   // Crear los encabezados para indicar que se enviará un JSON
	   HttpHeaders headers = new HttpHeaders();
	   headers.setContentType(MediaType.APPLICATION_JSON);
	   
	   // Crear la entidad HttpEntity con el cuerpo (parametros) y los encabezados
	   HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parametros, headers);

	   // URL de tu API Gateway que invoca la Lambda
	   String url = "https://mrkbrtnhuf.execute-api.us-east-2.amazonaws.com/imagen";

	   try {
		   // Llamar a la API de Lambda usando postForObject
		   ResponseEntity<String> response = restTemplate.exchange(
			   url, 
			   HttpMethod.POST, 
			   requestEntity, 
			   String.class
		   );
		   
		   // Obtener la respuesta del cuerpo de la respuesta
		   String responseBody = response.getBody();
		   System.out.println("Response: " + responseBody);

		   TextDTO textDTO = new TextDTO(convert(responseBody));
		   return textDTO;

	   } catch (Exception e) {
		   System.err.println("Error al invocar la API de Lambda: " + e.getMessage());
		   e.printStackTrace();
		   return null;
	   }
   }

    public userDTO crearUserDTO(String[] array) {

        return new userDTO(array[1], array[0], array[2], array[3], array[4], array[5], array[6], array[7]);
    } 

    public String convert(String s) {
		
		HashMap<String, Character> map = new HashMap<>();
		map.put("u00c3\\u0093" , 'Á');
		map.put("u00c3\\u00a1" , 'á');
		map.put("u00c3\\u0089" , 'É');
		map.put("u00c3\\u00a9" , 'é');
		map.put("u00c3\\u008d" , 'Í');
		map.put("u00c3\\u00ad" , 'í');
		map.put("u00c3\\u0093" , 'Ó');
		map.put("u00c3\\u00b3" , 'ó');
		map.put("u00c3\\u009a" , 'Ú');
		map.put("u00c3\\u00ba" , 'ú');
		map.put("u00c3\\u0091" , 'Ñ');
		map.put("u00c3\\u00b1" , 'ñ');
		
		StringBuilder resul = new StringBuilder();
		StringBuilder uniCode = new StringBuilder();
		
		int y = 0;
		for(int i = 1; i < s.length(); i++) {
			if(s.charAt(i - 1) == '\\' && s.charAt(i) == 'u') {
				
				for(y = i; y < i + 11; y++) {
					uniCode.append(s.charAt(y));
				}
				i = y;
				if(map.containsKey(uniCode.toString()))
				{
					resul.append(map.get(uniCode.toString()));
				}
				uniCode.setLength(0);
			}
			else {
				resul.append(s.charAt(i - 1));
			}
		}
		return resul.toString();
	}
}
