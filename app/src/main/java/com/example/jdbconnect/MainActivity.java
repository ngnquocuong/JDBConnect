package com.example.jdbconnect;

import static android.app.ProgressDialog.show;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Connection connection;

    String str;

    StringBuilder namesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ConnectionClass connectionClass = new ConnectionClass();
        connection = connectionClass.CONN();
        connect();
    }

    public void connect() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                if (connection == null) {
                    str = "Error";
                } else {
                    str = "Connected";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            runOnUiThread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            });
        });
    }

    public void btnConnect(View view) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                if (connection == null) {
                    showToast("Error connecting to database");
                    return;
                }

                // Query to get user names
                String query = "SELECT TOP (1000) [Name] FROM [ldbc].[dbo].[user]";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                namesList = new StringBuilder();
                while (resultSet.next()) {
                    namesList.append(resultSet.getString("Name")).append("\n"); // Append each name
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                showToast("Connection Error: " + e.getMessage());
            }

            runOnUiThread(() -> {
                TextView textView = findViewById(R.id.textView);
                textView.setText(namesList.toString()); // Update TextView with names
            });
        });
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}