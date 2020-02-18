/*
 * Copyright (C) 2020 Perin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.jpe.traynotifier;

import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * ...
 */
public class TrayMouseListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Tray Icon - Mouse clicked!");
        TrayApplication.get().getTray().displayMessage("Test", "Test Message! It worked :D", TrayIcon.MessageType.INFO);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Tray Icon - Mouse pressed!");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Tray Icon - Mouse released!");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("Tray Icon - Mouse entered!");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("Tray Icon - Mouse exited!");
    }

}
