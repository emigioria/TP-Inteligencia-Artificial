/*
 * Copyright 2007-2009 Georgina Stegmayer, Milagros Gutiérrez, Jorge Roa
 * y Milton Pividori.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package frsf.cidisi.faia.simulator.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimulatorEventNotifier {

	private static Map<EventType, Set<EventHandler>> eventHandlers = new HashMap<>();

	public static void runEventHandlers(EventType eventType, Object[] params) {

		if(!eventHandlers.keySet().contains(eventType)){
			return;
		}

		eventHandlers.get(eventType).stream().forEach(eventHandler -> eventHandler.runEventHandler(params));
	}

	public static void SubscribeEventHandler(EventType eventType, EventHandler eventHandler) {
		if(!eventHandlers.keySet().contains(eventType)){
			eventHandlers.put(eventType, new HashSet<EventHandler>());
		}

		eventHandlers.get(eventType).add(eventHandler);
	}

	public static void UnsubscribeEventHandler(EventType eventType, EventHandler eventHandler) {
		if(!eventHandlers.keySet().contains(eventType)){
			return;
		}

		eventHandlers.get(eventType).remove(eventHandler);
	}

	public static void ClearEventHandlers() {
		eventHandlers.clear();
	}
}
