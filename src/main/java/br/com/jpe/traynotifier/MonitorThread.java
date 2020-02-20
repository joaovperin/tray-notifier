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

import java.util.Date;
import java.util.List;

/**
 * ...
 */
public class MonitorThread implements Runnable {

    private static final int DELAY = 1200;
    private static final int SLEEP_TIME = 100;

    private static Thread monitorThread;
    private static boolean isRunning = true;
    private Date lastChecked;

    public static final void startMonitor() {
        if (monitorThread == null) {
            monitorThread = new Thread(new MonitorThread());
        }
        monitorThread.start();
    }

    public static final void stopMonitor() {
        isRunning = false;
    }

    @Override
    public void run() {
        lastChecked = new Date();
        final int targetCount = DELAY / SLEEP_TIME;
        int count = 0;
        while (true) {
            // Exit paragraph if not running
            if (!isRunning) {
                return;
            }
            // If reaches the targetCount, resets the counter and do the magic
            if (--count <= 0) {
                count = targetCount;
                doStuff();
            }
            // Sleep, young boy
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ex) {
            }

        }
    }

    private void doStuff() {
        List<NotificationMessage> newNotifications = FirestoreUtils.get().getNewNotifications(lastChecked);
        lastChecked = new Date();
        newNotifications.stream()
                .peek(TrayApplication.get()::showNotification)
                .forEach(FirestoreUtils.get()::removeNotification);
    }

}
