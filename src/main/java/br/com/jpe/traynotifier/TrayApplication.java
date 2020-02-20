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

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;

/**
 * ...
 */
public class TrayApplication extends SwingApplication {

    private static final TrayApplication instance;

    private TrayIcon trayIcon;

    static {
        instance = new TrayApplication();
    }

    private TrayApplication() {
    }

    public static TrayApplication get() {
        return instance;
    }

    @Override
    public void install() {

        if (SystemTray.isSupported()) {

            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("src/main/resources/tray.png");

            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener((ActionEvent e) -> {
                System.out.println("Exiting...");
                System.exit(0);
            });
            popup.add(defaultItem);

            trayIcon = new TrayIcon(image, "TrayNotifier by Joaovperin", popup);

            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener((ActionEvent e) -> {
                trayIcon.displayMessage("Action Event",
                        "An Action Event Has Been Performed!",
                        TrayIcon.MessageType.INFO);
            });
            trayIcon.addMouseListener(new TrayMouseListener());

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        } else {

            System.out.println("sorry");
//  System Tray is not supported
        }
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public void showNotification(NotificationMessage notification) {
        String title = String.format("%s", sdf.format(notification.getDate()));
        trayIcon.displayMessage(title, notification.getMessage(), TrayIcon.MessageType.INFO);
    }

    public TrayIcon getTray() {
        if (trayIcon == null) {
            throw new UnsupportedOperationException("Trayicon is null");
        }
        return trayIcon;
    }

}
