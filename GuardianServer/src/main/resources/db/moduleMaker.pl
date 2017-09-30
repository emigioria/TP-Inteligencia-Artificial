% Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
% This Source Code Form is subject to the terms of the Mozilla Public
% License, v. 2.0. If a copy of the MPL was not distributed with this
% file, You can obtain one at http://mozilla.org/MPL/2.0/.

% Autor: Rico Andrés, Rebechi Esteban, Gioria Emiliano.
% Fecha: 24/06/2017

:- dynamic(tieneRiesgo/3).
:- dynamic(escuchada/2).
:- dynamic(riesgo/2).
:- dynamic(limiteRiesgo/2).
:- dynamic(accion/1).
:- dynamic(sospecho/1).
:- dynamic(noSospecho/1).
:- dynamic(critica/2).
:- dynamic(clasificada/2).

make_module(AgID, FileInit) :- read_file_to_terms(FileInit, Terms, []), assert_in_module(Terms, AgID).
assert_in_module([],_).
assert_in_module([T|R], ID) :- assert(ID:T), assert_in_module(R,ID).
