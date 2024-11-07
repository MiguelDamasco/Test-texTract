package com.example.demo.Services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.DTO.userDTO;

@Service
public class textService {
    
    @Autowired
    private RestTemplate restTemplate;

    public userDTO getText() {
        String response = restTemplate.getForObject("https://bo56yc8pwe.execute-api.us-east-2.amazonaws.com/default/prueba", String.class);
        String[] resul = AlgoritmoConersion.obtenerDatos(convert(response));
        userDTO user = crearUserDTO(resul);
        return user;
    }

    public userDTO crearUserDTO(String[] array) {

        return new userDTO(array[0], array[1], array[2], array[3], array[4], array[5], array[6], array[7]);
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
