package com.clinica.sonrisafeliz;

import com.clinica.sonrisafeliz.controlador.ControladorOdontologo;
import com.clinica.sonrisafeliz.controlador.ControladorPaciente;
import com.clinica.sonrisafeliz.controlador.ControladorTurno;
import com.clinica.sonrisafeliz.presentacion.consola.ConsolaUtils;
import com.clinica.sonrisafeliz.presentacion.consola.MenuOdontologos;
import com.clinica.sonrisafeliz.presentacion.consola.MenuPacientes;
import com.clinica.sonrisafeliz.presentacion.consola.MenuPrincipal;
import com.clinica.sonrisafeliz.presentacion.consola.MenuTurnos;
import com.clinica.sonrisafeliz.repositorio.RepositorioOdontologo;
import com.clinica.sonrisafeliz.repositorio.RepositorioPaciente;
import com.clinica.sonrisafeliz.repositorio.RepositorioTurno;
import com.clinica.sonrisafeliz.servicio.ServicioOdontologo;
import com.clinica.sonrisafeliz.servicio.ServicioPaciente;
import com.clinica.sonrisafeliz.servicio.ServicioTurno;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Repositorios
        RepositorioPaciente repoPaciente = new RepositorioPaciente();
        RepositorioOdontologo repoOdontologo = new RepositorioOdontologo();
        RepositorioTurno repoTurno = new RepositorioTurno();

        // Servicios
        ServicioPaciente servicioPaciente = new ServicioPaciente(repoPaciente, repoTurno);
        ServicioOdontologo servicioOdontologo = new ServicioOdontologo(repoOdontologo, repoTurno);
        ServicioTurno servicioTurno = new ServicioTurno(repoTurno, servicioPaciente, servicioOdontologo);

        // Controladores
        ControladorPaciente controladorPaciente = new ControladorPaciente(servicioPaciente);
        ControladorOdontologo controladorOdontologo = new ControladorOdontologo(servicioOdontologo);
        ControladorTurno controladorTurno = new ControladorTurno(servicioTurno);

        // Presentación
        ConsolaUtils utils = new ConsolaUtils(new Scanner(System.in));
        MenuPacientes menuPacientes = new MenuPacientes(controladorPaciente, utils);
        MenuOdontologos menuOdontologos = new MenuOdontologos(controladorOdontologo, utils);
        MenuTurnos menuTurnos = new MenuTurnos(controladorTurno, menuPacientes, menuOdontologos, utils);
        MenuPrincipal menu = new MenuPrincipal(menuPacientes, menuOdontologos, menuTurnos);

        menu.iniciar();
    }
}
