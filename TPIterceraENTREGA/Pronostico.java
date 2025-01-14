package main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Pronostico {
	private ResultadoEnum resultadoApuesta;
	private Partido partido;
	private Persona persona;
	
	public Pronostico () {	
	}

	public Pronostico ( ResultadoEnum resultadoApuesta, Partido partido,  Persona persona) {
		this.resultadoApuesta=resultadoApuesta;
		this.partido=partido;
		this.persona = persona;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public ResultadoEnum getResultadoApuesta() {
		return resultadoApuesta;
	}

	public void setResultadoApuesta(ResultadoEnum resultadoApuesta) {
		this.resultadoApuesta = resultadoApuesta;
	}

	public Partido getPartido() {
		return partido;
	}

	public void setPartido(Partido partido) {
		this.partido = partido;
	}


	public static void leerpronostico(ArrayList<Persona> personas, ArrayList<Pronostico> pronostico, Connection con) {
		try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from tabla_partidos");
			int i=0;
			while(rs.next())			
			 { 
				String persona        = rs.getString(2);  //leo nombre 
				Persona personaAdd = new Persona(persona,0); // para agregar persona
				
				if (i==0) {
					personaAdd.setNombre(persona); personaAdd.setPuntos(0);personas.add(personaAdd); //agrego persona
					
				}
				else  { int censo =0;
					for(int j=0; j<personas.size();j++)
						{
							if (personas.get(j).getNombre().equals(personaAdd.getNombre())) 	
								{	 
									censo=censo+1;} // marco la que esta y no la agrego 
									}
					if (censo==0) {personaAdd.setNombre(persona); personaAdd.setPuntos(0);personas.add(personaAdd);}// si no lo encuentro lo agrego
					}
							 												 
				String resultado1a    = "EMPATE";  //leo y guardo valores
				String resultado2a    = "EMPATE";
			    String gana1          = rs.getString(4);
			    String empata         = rs.getString(5);
			    String gana2          = rs.getString(6);
			    String equipA1          = rs.getString(3);
			    String equipA2          = rs.getString(7);
			    Equipo equipoA1 = new Equipo(equipA1,"");                        // armo partido
			    Equipo equipoA2 = new Equipo(equipA2,"");						//
			    Partido partAdd = new Partido (equipoA1,equipoA2,0 ,0);			//
			   
			   
			   if (!empata.isEmpty())  {resultado1a="EMPATE";resultado2a="EMPATE";} //si no esta vacio es empate hay "x"
			   else {
				   	if (gana2.isEmpty()&&empata.isEmpty()) // e2 vacio gana e1
					{resultado1a = "GANA";resultado2a = "PIERDE";}                       //		SISTEMA DE PUNTUACION
				   	else
					{resultado1a = "PIERDE";resultado2a = "GANA";}                      
			   		}
			   
			   	//variables intermedias para crear la lista pronostico y resultadoenum(delaapuesta)
				ResultadoEnum apuesta = new ResultadoEnum(resultado1a,resultado2a);  //armo clases y las añado a la lista
				apuesta.setResultadoEqu1(resultado1a);//ok
				apuesta.setResultadoEqu2(resultado2a);	   
				Pronostico pronosticoAdd= new Pronostico(apuesta, partAdd,personaAdd); //hay 4 partidos y 8 apuestas
				pronosticoAdd.setResultadoApuesta(apuesta);//ok 							
				pronosticoAdd.setPartido(partAdd);// el partido ya lo tenia el real, aunque se podria formar aqui
				pronosticoAdd.setPersona(personaAdd);
				pronostico.add(pronosticoAdd);
				
			   i=i+1;
			   
			  }
			   
			} catch (SQLException e) {
				System.out.println("error SQL...");
				e.printStackTrace();
			}  
			 
		   System.out.println("---------");
		   System.out.println("apuestas");
		   System.out.println("        ");
		   for (int indice2 =0; indice2<pronostico.size();indice2++) {
		   System.out.println(pronostico.get(indice2).getPartido().getEquipo1().getEquipo()+ " "+pronostico.get(indice2).getPartido().getEquipo2().getEquipo()+" "+pronostico.get(indice2).getPersona().getNombre()+" "+pronostico.get(indice2).getResultadoApuesta().getResultadoEqu1()+" "+pronostico.get(indice2).getResultadoApuesta().getResultadoEqu2());
		   }
		   System.out.println("---------");

		
	}
}
