#
# Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#

﻿% Autor: Rico Andrés, Rebechi Esteban, Gioria Emiliano.
% Fecha: 24/06/2017

%Inicialización predicados delitoHogar
tieneRiesgo(delitoHogar, ayuda, 3).
critica(delitoHogar, ayuda).
tieneRiesgo(delitoHogar, policia, 3).
tieneRiesgo(delitoHogar, disparo, 4).
tieneRiesgo(delitoHogar, bala, 3).
tieneRiesgo(delitoHogar, ladron, 3).
tieneRiesgo(delitoHogar, alarma, 2).
tieneRiesgo(delitoHogar, gato, 1).
tieneRiesgo(delitoHogar, chorro, 3).
tieneRiesgo(delitoHogar, piso, 1).
tieneRiesgo(delitoHogar, dar_plata, 6).
critica(delitoHogar, dar_plata).
tieneRiesgo(delitoHogar, dar_todo, 6).
critica(delitoHogar, dar_todo).
tieneRiesgo(delitoHogar, vaciar_caja, 6).
critica(delitoHogar, vaciar_caja).
tieneRiesgo(delitoHogar, dar_joyas, 6).
critica(delitoHogar, dar_joyas).
tieneRiesgo(delitoHogar, poner_todo_bolsa, 4).
tieneRiesgo(delitoHogar, esto_ser_asalto, 7).
critica(delitoHogar, esto_ser_asalto).
tieneRiesgo(delitoHogar, esto_ser_robo, 7).
critica(delitoHogar, esto_ser_robo).
tieneRiesgo(delitoHogar, cortar, 2).
tieneRiesgo(delitoHogar, rayar, 2).
tieneRiesgo(delitoHogar, pinchar, 2).
tieneRiesgo(delitoHogar, quemar, 2).

%Inicialización predicados delitoCallejero
tieneRiesgo(delitoCallejero, ayuda, 3).
critica(delitoCallejero, ayuda).
tieneRiesgo(delitoCallejero, eameo, 1).
tieneRiesgo(delitoCallejero, dar_todo, 6).
critica(delitoCallejero, dar_todo).
tieneRiesgo(delitoCallejero, dar_celular, 6).
critica(delitoCallejero, dar_celular).
tieneRiesgo(delitoCallejero, dar_mochila, 6).
critica(delitoCallejero, dar_mochila).
tieneRiesgo(delitoCallejero, dar_moto, 6).
critica(delitoCallejero, dar_moto).
tieneRiesgo(delitoCallejero, dar_bici, 6).
critica(delitoCallejero, dar_bici).
tieneRiesgo(delitoCallejero, dar_billetera, 6).
critica(delitoCallejero, dar_billetera).
tieneRiesgo(delitoCallejero, dar_cartera, 6).
critica(delitoCallejero, dar_cartera).
tieneRiesgo(delitoCallejero, policia, 3).
critica(delitoCallejero, policia).
tieneRiesgo(delitoCallejero, droga, 1).
tieneRiesgo(delitoCallejero, faso, 1).
tieneRiesgo(delitoCallejero, merca, 1).
tieneRiesgo(delitoCallejero, cortar, 2).
tieneRiesgo(delitoCallejero, rayar, 2).
tieneRiesgo(delitoCallejero, bala, 3).
tieneRiesgo(delitoCallejero, yuta, 1).
tieneRiesgo(delitoCallejero, fierro, 2).
tieneRiesgo(delitoCallejero, ladron, 3).
tieneRiesgo(delitoCallejero, chorro, 3).

%Inicialización predicados violenciaDomestica
tieneRiesgo(violenciaDomestica, inutil, 3).
tieneRiesgo(violenciaDomestica, idiota, 3).
tieneRiesgo(violenciaDomestica, pegar, 3).
critica(violenciaDomestica, pegar).
tieneRiesgo(violenciaDomestica, callar, 2).
tieneRiesgo(violenciaDomestica, no_golpear, 6).
critica(violenciaDomestica, no_golpear).
tieneRiesgo(violenciaDomestica, soltar, 5).
critica(violenciaDomestica, soltar).
tieneRiesgo(violenciaDomestica, basta, 1).
tieneRiesgo(violenciaDomestica, perdonar, 1).
tieneRiesgo(violenciaDomestica, basura, 3).
tieneRiesgo(violenciaDomestica, inservible, 3).
tieneRiesgo(violenciaDomestica, acogotar, 7).
critica(violenciaDomestica, acogotar).
tieneRiesgo(violenciaDomestica, no_decir_nadie, 2).
tieneRiesgo(violenciaDomestica, cerrar_boca, 3).
tieneRiesgo(violenciaDomestica, sacar_manos_encima, 7).
critica(violenciaDomestica, sacar_manos_encima).
tieneRiesgo(violenciaDomestica, venir_aca, 3).

%Inicialización predicados emergeenciaMedica
tieneRiesgo(emergenciaMedica, ayuda, 3).
critica(emergenciaMedica, ayuda).
tieneRiesgo(emergenciaMedica, herido, 3).
tieneRiesgo(emergenciaMedica, llamar_policia, 3).
critica(emergenciaMedica, llamar_policia).
tieneRiesgo(emergenciaMedica, llamar_ambulancia, 6).
critica(emergenciaMedica, llamar_ambulancia).
tieneRiesgo(emergenciaMedica, medico, 3).
tieneRiesgo(emergenciaMedica, choque, 2).
tieneRiesgo(emergenciaMedica, accidente, 2).
tieneRiesgo(emergenciaMedica, infarto, 4).
tieneRiesgo(emergenciaMedica, descompensar, 3).
tieneRiesgo(emergenciaMedica, emergencia, 4).
critica(emergenciaMedica, emergencia).
tieneRiesgo(emergenciaMedica, hospital, 3).
critica(emergenciaMedica, hospital).
tieneRiesgo(emergenciaMedica, sangre, 3).
tieneRiesgo(emergenciaMedica, corte, 2).
tieneRiesgo(emergenciaMedica, desangrar, 4).
critica(emergenciaMedica, desangrar).

%Inicialización predicados incendio
tieneRiesgo(incendio, fuego, 4).
critica(incendio, fuego).
tieneRiesgo(incendio, humo, 4).
critica(incendio, humo).
tieneRiesgo(incendio, ayuda, 3).
tieneRiesgo(incendio, herido, 3).
tieneRiesgo(incendio, incendio, 4).
critica(incendio, incendio).
tieneRiesgo(incendio, llamar_policia, 3).
tieneRiesgo(incendio, llamar_ambulancia, 4).
critica(incendio, llamar_ambulancia).
tieneRiesgo(incendio, llamar_bomberos, 6).
critica(incendio, llamar_bomberos).
tieneRiesgo(incendio, emergencia, 4).
critica(incendio, emergencia).
tieneRiesgo(incendio, hospital, 3).
critica(incendio, hospital).
tieneRiesgo(incendio, quemar, 3).
tieneRiesgo(incendio, quemadura, 3).

%Inicialización predicados explosion
tieneRiesgo(explosion, fuego, 3).
tieneRiesgo(explosion, ayuda, 3).
tieneRiesgo(explosion, herido, 3).
tieneRiesgo(explosion, llamar_policia, 5).
critica(explosion, llamar_policia).
tieneRiesgo(explosion, llamar_ambulancia, 5).
critica(explosion, llamar_ambulancia).
tieneRiesgo(explosion, llamar_bomberos, 5).
critica(explosion, llamar_bomberos).
tieneRiesgo(explosion, explosion, 4).
critica(explosion, explosion).
tieneRiesgo(explosion, explotar, 4).
critica(explosion, explotar).
tieneRiesgo(explosion, emergencia, 4).
critica(explosion, emergencia).
tieneRiesgo(explosion, hospital, 3).
tieneRiesgo(explosion, ataque, 3).
tieneRiesgo(explosion, bomba, 4).
critica(explosion, bomba).

%Inicialización límites de riesgo
limiteRiesgo(delitoHogar, 10).
limiteRiesgo(delitoCallejero, 10).
limiteRiesgo(violenciaDomestica, 10).
limiteRiesgo(incendio, 10).
limiteRiesgo(emergenciaMedica, 10).
limiteRiesgo(explosion, 10).

%Inicialización contadores de riesgo
riesgo(delitoHogar, 0).
riesgo(delitoCallejero, 0).
riesgo(violenciaDomestica, 0).
riesgo(incendio, 0).
riesgo(emergenciaMedica, 0).
riesgo(explosion, 0).

%Inicialización del estado de los incidentes
noSospecho(delitoHogar).
noSospecho(delitoCallejero).
noSospecho(violenciaDomestica).
noSospecho(incendio).
noSospecho(emergenciaMedica).
noSospecho(explosion).

:- dynamic(tieneRiesgo/3).
:- dynamic(escuchada/2).
:- dynamic(riesgo/2).
:- dynamic(limiteRiesgo/2).
:- dynamic(accion/1).
:- dynamic(sospecho/1).
:- dynamic(noSospecho/1).
:- dynamic(critica/2).
:- dynamic(clasificada/2).
