/*
 * Copyright (C) 2020 Joaov
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

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * ...
 */
public class FirestoreUtils {

    private static final String NOTIFICATIONS_TABLE = "notifications";

    private static final FirestoreUtils instance;
    private Firestore db;

    static {
        instance = new FirestoreUtils();
    }

    private FirestoreUtils() {
    }

    public static FirestoreUtils get() {
        return instance;
    }

    public final void init() throws IOException {
        try ( InputStream serviceAccount = new FileInputStream("etc/google-services.json")) {
            FirebaseApp.initializeApp(new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://tray-notifier.firebaseio.com")
                    .build());
        }
        db = FirestoreClient.getFirestore();
    }

    public List<NotificationMessage> getNewNotifications(Date lastChecked) {
        // asynchronously retrieve all users
        ApiFuture<QuerySnapshot> query = db.collection(NOTIFICATIONS_TABLE)
                .whereLessThanOrEqualTo("date", lastChecked)
                .get();
        // ...
        // query.get() blocks on response
        QuerySnapshot querySnapshot = getSynchronous(query);
        return querySnapshot.getDocuments().stream()
                .map(e -> new NotificationMessage(
                e.getId(),
                e.getString("message"),
                e.getDate("date")
        )).collect(Collectors.toList());
    }

    public void removeNotification(NotificationMessage notification) {
        // remove the notification
        ApiFuture<WriteResult> delete = db.collection(NOTIFICATIONS_TABLE)
                .document(notification.getId())
                .delete();
        getSynchronous(delete);
    }

    private <T> T getSynchronous(ApiFuture<T> result) {
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
