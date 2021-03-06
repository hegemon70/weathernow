/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nett.formacion.aaa.module4.spring.weathernow.controller;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import nett.formacion.aaa.module4.spring.weathernow.model.Ciudad;
import nett.formacion.aaa.module4.spring.weathernow.model.Estadoscielo;
import nett.formacion.aaa.module4.spring.weathernow.model.Pais;
import nett.formacion.aaa.module4.spring.weathernow.model.Registro;
import nett.formacion.aaa.module4.spring.weathernow.model.Usuario;
import nett.formacion.aaa.module4.spring.weathernow.pojos.Escala;
import nett.formacion.aaa.module4.spring.weathernow.pojos.Forecast;
import nett.formacion.aaa.module4.spring.weathernow.pojos.GetSky;
import nett.formacion.aaa.module4.spring.weathernow.pojos.GetTemperature;
import nett.formacion.aaa.module4.spring.weathernow.repo.WeatherNowCitiesRepo;
import nett.formacion.aaa.module4.spring.weathernow.repo.WeatherNowEstadoscieloRepo;
import nett.formacion.aaa.module4.spring.weathernow.repo.WeatherNowRepository;
import nett.formacion.aaa.module4.spring.weathernow.repo.WeatherNowUsuarioRepo;

/**
 *
 * @author Miguel Ángel Buñuales, Verónica Torcal, Juan Nonay, Mª Carmen Chaves
 */
@RestController
public class WeatherNowRestController {

	@Autowired
	private WeatherNowRepository wnRepo;
	@Autowired
	private WeatherNowCitiesRepo wnCityRepo;
	@Autowired
	private WeatherNowEstadoscieloRepo wnEstadosRepo;
	@Autowired
	private WeatherNowUsuarioRepo wnUsuarioRepo;	

	// Objeto que convertirá nuestros objetos en cadenas de texto JSON para
	// devolverlas
	ObjectMapper mapper = new ObjectMapper();

	private static final Logger LOGGER = LogManager.getLogger(WeatherNowRestController.class.getName());

	/**
	 * Método para obtener las ciudades incluídas en la BBDD
	 */
	// Anotación para permitir llamar a la API REST desde orígenes distintos a localhost:7777 (o el puerto que sea)
	@CrossOrigin
	// Anotación para indicar la dirección que ejecutará el método, devuelve
	// texto
	@RequestMapping(path = "/wn/getCities", produces = { "text/plain", "application/json" })
	// Anotación que indica que devolveremos un Objeto, en este caso, un objeto
	// de tipo String.
	@ResponseBody
	public String getCities() {

		String response = null;

		List<Ciudad> ciudades = new ArrayList<>();

		try {

			ciudades = (List<Ciudad>) wnCityRepo.findAll();
			response = mapper.writeValueAsString(ciudades);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			response = e.getMessage();
		}

		return response;
	}

	/**
	 * Método para introducir un estado del cielo en la BBDD Autor:Miguel Ángel
	 * Buñuales
	 * 
	 * @param fecha   no obligatorio, si no se introduce se supone que es la fecha actual.
	 * @param ciudad Obligatorio
	 * @param sky Obligatorio
	 * @param idUsuario No obligatorio. Si no se envía se usará el usuario con id=1 ¿? - decidir esto entre todos
	 * @param password No obligatorio. Si no se envía se usará el pass del usuario con id=1 ¿? - decidir esto entre todos
	 * @return
	 */
	// Anotación para permitir llamar a la API REST desde orígenes distintos a localhost:7777 (o el puerto que sea)
	@CrossOrigin
	// Anotación para indicar la dirección que ejecutará el método, devuelve
	// texto
	@RequestMapping(path = "/addSky", produces = { "text/plain", "application/json" })
	// Anotación que indica que devolveremos un Objeto, en este caso, un objeto
	// de tipo String.
	@ResponseBody
	public String addSky(
			@RequestParam(value = "fecha", required = false) String fecha,
			@RequestParam(value = "ciudad", required = true) String ciudad,
			@RequestParam(value = "sky", required = true) Integer sky// ,
	// @RequestParam(value = "nombreUsu", required = true) String nombreUsu,
	// @RequestParam(value = "password", required = true) String password
	) {

		System.out.println("Dentro del método addSky de WheatherNowController");

		// Inicializamos una respuesta
		String response = null;
		// Instanciamos una ciudad ( vacía por ahora )
		Ciudad city = new Ciudad();
		// Instanciamos una estado del cielo ( vacío por ahora )
		Estadoscielo estado = new Estadoscielo();
		// Instanciamos un usuario ( vacío por ahora )
		Usuario usuario = new Usuario();
		// Instanciamos un registro ( vacío por ahora )
		Registro registro = new Registro();
		// Pojo getSky
		GetSky getSky = new GetSky();
		// Objeto auxiliar para la codificación MD5
		// MessageDigest md5 = MessageDigest.getInstance("MD5");

		// Bloque try-catch en el que realizaremos todas las tareas de consulta
		// a la BBDD
		try {
			// Buscamos el nombre de usuario y comparamos el password con el del
			// objeto encontrado
			// usuario = wnUsuarioRepo.findByNombreUsuIgnoreCase(nombreUsu);
			// String passwordMD5 = (new
			// HexBinaryAdapter()).marshal(md5.digest(password.getBytes()));
			// if(usuario != null && passwordMD5){

			// }

			// Cargamos la ciudad cuyo nombre coinincida con el parámetro
			// consultando, la BBDD a través del repositorio de Ciudades
			city = wnCityRepo.findByNombreCiuIgnoreCase(ciudad);
			// Si encuentra la ciudad
			if (city != null) {
				System.out.println(city.getNombreCiu());
				// Cargamos El estdo del cielo cuyo id coinincida con el
				// parámetro, consultando la BBDD a través del repositorio de
				// estadoscielo
				estado = wnEstadosRepo.findOne(sky);
				System.out.println(estado.getEstado());
				// Vamos cargando los objetos consultados a la base de datos en
				// el objeto -- registro --
				registro.setCiudade(city);
				registro.setEstadoscielo(estado);
				registro.setFechaReg(new Date());
				/* if(idUsuario == null){ */
				Integer idUsuario = 1;
				// }
				usuario = wnUsuarioRepo.findOne(idUsuario);
				registro.setUsuario(usuario);
				// Guardamos el registro
				wnRepo.save(registro);
				// Devolvemos el registro
				// response = mapper.writeValueAsString(registro);

				// Devolvemos un pojo de tipo GetSky
				getSky.setEstadocielo(registro.getEstadoscielo().getEstado());
				response = mapper.writeValueAsString(getSky);
				// Si no encuentra la ciudad no guarda.
			} else {
				response = "Esta ciudad no existe en nuestra base de datos.";
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			response = e.getMessage();
		}

		return response;
	}

	/**
	 * Método para introducir una temperatura relativa a una ciudad en la BBDD
	 * Autor:Miguel Ángel Buñuales
	 * 
	 * @param fecha   no obligatorio, si no se introduce se supone que es la fecha actual.
	 * @param ciudad Obligatorio
	 * @param sky Obligatorio
	 * @param idUsuario Obligatorio.
	 * @return
	 */
	// Anotación para permitir llamar a la API REST desde orígenes distintos a localhost:7777 (o el puerto que sea)
	@CrossOrigin
	// Anotación para indicar la dirección que ejecutará el método, devuelve
	// texto
	@RequestMapping(path = "/addTemperature", produces = { "text/plain", "application/json" })
	// Anotación que indica que devolveremos un Objeto, en este caso, un objeto
	// de tipo String.
	@ResponseBody
	public String addSky(
			@RequestParam(value = "fecha", required = false) String fecha,
			@RequestParam(value = "ciudad", required = true) String ciudad,
			// @RequestParam(value = "usuario", required = true) Integer usuario,			
			// @RequestParam(value = "password", required = true) String password,			
			@RequestParam(value = "temperatura", required = true) Float temperatura,
			@RequestParam(value = "escala", required = true) String escala) {

		System.out.println("Dentro del método addTemperature de WheatherNowController");
		// Inicializamos una respuesta
		String response = null;
		// Nos aseguramos que la escala solicitada está dentro de nuestro ENUM
		if (Escala.contains(escala)) {

			// Instanciamos una ciudad ( vacía por ahora )
			Ciudad city = new Ciudad();
			// Instanciamos un usuario ( vacío por ahora )
			Usuario usuario = new Usuario();
			// Instanciamos un registro ( vacío por ahora )
			Registro registro = new Registro();
			// Objeto que convertirá nuestros objetos en cadenas de texto JSON
			// para
			// devolverlas
			ObjectMapper mapper = new ObjectMapper();

			// Bloque try-catch en el que realizaremos todas las tareas de
			// consulta
			// a la BBDD
			try {
				// Cargamos la ciudad cuyo nombre coinincida con el parámetro
				// consultando, la BBDD a través del repositorio de Ciudades
				city = wnCityRepo.findByNombreCiuIgnoreCase(ciudad);
				// Si encuentra la ciudad
				if (city != null) {
					
					/*
					 * Aquí hay que meter el código.
					 */

				} else {
					response = "Esta ciudad no existe en nuestra base de datos.";
				}

			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				response = e.getMessage();
			}
		} else {
			response = "Wrong temperature Scale";
		}

		return response;
	}

	/**
	 * Método para introducir una temperatura relativa a una ciudad en la BBDD
	 * Autor:Miguel Ángel Buñuales
	 * 
	 * @param ciudad Obligatorio
	 * @param escala Obligatorio
	 * @return
	 */	
	// Anotación para permitir llamar a la API REST desde orígenes distintos a localhost:7777 (o el puerto que sea)
	@CrossOrigin
	@RequestMapping(path = "/getForecast", produces = { "text/plain", "application/json" })	
	@ResponseBody
	public String getForecast(@RequestParam(value = "ciudad", required = true) String ciudad,
						 	  @RequestParam(value = "escala", required = true) String escala) {

		System.out.println("Dentro del método getForecast de WheatherNowController");

		// Inicializamos una respuesta
		String response = null;
		// Dias de pronóstico
		int fDays = 7;
		// Nos aseguramos que la escala solicitada está dentro de nuestro ENUM
		if (Escala.contains(escala)) {
			// Instanciamos una ciudad ( vacía por ahora )
			Ciudad city = new Ciudad();
			// Instanciamos un registro ( vacío por ahora )
			Registro registro = new Registro();
			// Pojos para la respuesta
			GetSky cielo = new GetSky();
			GetTemperature temp = new GetTemperature();
			List<Forecast> forecastArray = new ArrayList<>();
			// Objeto que convertirá nuestros objetos en cadenas de texto JSON
			// para devolverlas
			ObjectMapper mapper = new ObjectMapper();
			// Instanciamos el calendario
			Calendar hoy = Calendar.getInstance();
			// Bloque try-catch en el que realizaremos todas las tareas de
			// consulta a la BBDD
			try {

				// Cargamos la ciudad cuyo nombre coinincida con el parámetro
				// consultando, la BBDD a través del repositorio de Ciudades
				city = wnCityRepo.findByNombreCiuIgnoreCase(ciudad);
				// Si encuentra la ciudad
				if (city != null) {
					Forecast f = new Forecast();
					for(int i=0;i<fDays;i++){
						hoy.add(Calendar.DATE, i);		
						System.out.println(hoy.getTime());
						System.out.println(city);
						registro = wnRepo.findByFechaRegAndCiudade(hoy.getTime(), city);
						System.out.println(registro);
						cielo.setEstadocielo(registro.getEstadoscielo().getEstado());
						temp.setEscala(Escala.valueOf(escala));
						if(escala.toUpperCase().equals("F")){
							temp.setTemperatura((long) celsiusToFahrenheit(registro.getTemperatura()));
						}else if(escala.toUpperCase().equals("K")){
							temp.setTemperatura((long) registro.getTemperatura()+273);
						}else{
							temp.setTemperatura((long) registro.getTemperatura());
						}
						
						f.setDay(hoy.getTime());
						f.setSky(cielo);
						f.setTemperature(temp);
						
						forecastArray.add(f);
						
					}
					
					
					

				} else {
					response = "Esta ciudad no existe en nuestra base de datos.";
				}

			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				response = e.getMessage();
			}
		} else {
			response = "Wrong temperature Scale";
		}

		return response;
	}
	
	public static double celsiusToFahrenheit(double celsius){
		return (9.0/5)*celsius+32;
	}

	public static double fahrenheitToCelsius(double fahrenheit){
		return fahrenheit - 32 * (5 / 9.0);
	}


}
