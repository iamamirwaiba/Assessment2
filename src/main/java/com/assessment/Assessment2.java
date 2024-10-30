package com.assessment;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Assessment2 extends Application {

	private final List<Long>       fibonacciSequence = new ArrayList<>();
	private final List<List<Long>> buckets           = new ArrayList<>();
	private       boolean          isDescendingSort  = false; // Track sorting order

	@Override
	public void start(Stage primaryStage) {
		TextField lengthInput = new TextField();
		lengthInput.setPromptText("Enter length (1-100)");
		List<Long> originalSequence = new ArrayList<>();

		Button fibGenButton = createStyledButton("Fib Gen");
		TextArea outputArea = new TextArea();
		outputArea.setEditable(false);

		Button filterEvenButton = createStyledButton("Filter Even");
		Button filterOddButton = createStyledButton("Filter Odd");
		Button saveButton = createStyledButton("Save to File");
		Button bucketSortButton = createStyledButton("Fib Bucket Sort");
		Button bucketSearchButton = createStyledButton("Fib Bucket Search");
		TextField multipleInput = new TextField();
		multipleInput.setPromptText("Enter multiple n");
		Button filterMultipleButton = createStyledButton("Filter by Multiple of n");
		TextField searchInput = new TextField();
		searchInput.setPromptText("Enter number to search");

		CheckBox descendingSortCheckbox = new CheckBox("Sort Descending");
		bucketSearchButton.setDisable(true); // Initially disabled
		bucketSortButton.setDisable(true);
		filterEvenButton.setDisable(true);
		filterOddButton.setDisable(true);
		saveButton.setDisable(true);
		filterMultipleButton.setDisable( true );

		fibGenButton.setOnAction(e -> {
			int length = Integer.parseInt(lengthInput.getText());
			if (length >= 0 && length <= 100) {
				generateFibonacci(length);
				outputArea.setText(fibonacciSequence.toString());
				originalSequence.addAll(fibonacciSequence);
				bucketSortButton.setDisable(false);
				saveButton.setDisable( false );
				filterEvenButton.setDisable( false );
				filterOddButton.setDisable( false );
				filterMultipleButton.setDisable( false );
			} else {
				showAlert("Please enter a length between 0 and 100.");
			}
		});

		filterEvenButton.setOnAction(e -> {
			isDescendingSort = descendingSortCheckbox.isSelected();
			List<Long> filtered = filterEven(fibonacciSequence);
			if(isDescendingSort) {
				filtered.sort( Comparator.reverseOrder() );
			}
			outputArea.setText(filtered.toString());
		});

		filterOddButton.setOnAction(e -> {
			isDescendingSort = descendingSortCheckbox.isSelected();
			List<Long> filtered = filterOdd(fibonacciSequence);
			if(isDescendingSort) {
				filtered.sort( Comparator.reverseOrder() );
			}
			outputArea.setText(filtered.toString());
		});

		bucketSortButton.setOnAction(e -> {
			isDescendingSort = descendingSortCheckbox.isSelected(); // Get sorting order
			showBucketSortWindow(fibonacciSequence);
			bucketSearchButton.setDisable(false); // Enable search button after sorting
		});

		filterMultipleButton.setOnAction(e -> {
			try {
				int n = Integer.parseInt(multipleInput.getText());
				List<Long> filtered = filterByMultiple(fibonacciSequence, n);
				outputArea.setText(filtered.toString());
			} catch (NumberFormatException ex) {
				showAlert("Please enter a valid integer for n.");
			}
		});

		// Search for a number in the buckets
		bucketSearchButton.setOnAction(e -> {
			try {
				long searchNumber = Long.parseLong(searchInput.getText());
				String result = searchInBuckets(searchNumber);
				outputArea.setText(result);
			} catch (NumberFormatException ex) {
				showAlert("Please enter a valid number to search.");
			}
		});

		saveButton.setOnAction(e -> saveToFile(outputArea.getText()));

		VBox layout = new VBox(10, lengthInput, fibGenButton, outputArea, descendingSortCheckbox,filterEvenButton, filterOddButton,
				multipleInput, filterMultipleButton, bucketSortButton,
				searchInput, bucketSearchButton, saveButton);
		Scene scene = new Scene(layout, 400, 500);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Fibonacci Generator");
		primaryStage.show();
	}

	private Button createStyledButton(String text) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: #4CAF50; " +
				"-fx-text-fill: white; " +
				"-fx-padding: 10px; " +
				"-fx-font-size: 14px; " +
				"-fx-border-radius: 5px; " +
				"-fx-background-radius: 5px; " +
				"-fx-font-weight: bold;");
		button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45a049; " +
				"-fx-text-fill: white; " +
				"-fx-padding: 10px; " +
				"-fx-font-size: 14px; " +
				"-fx-border-radius: 5px; " +
				"-fx-background-radius: 5px; " +
				"-fx-font-weight: bold;"));
		button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; " +
				"-fx-text-fill: white; " +
				"-fx-padding: 10px; " +
				"-fx-font-size: 14px; " +
				"-fx-border-radius: 5px; " +
				"-fx-background-radius: 5px; " +
				"-fx-font-weight: bold;"));
		return button;
	}

	private void generateFibonacci(int length) {
		fibonacciSequence.clear();
		long a = 0, b = 1;
		for (int i = 0; i < length; i++) {
			fibonacciSequence.add(a);
			long next = a + b;
			a = b;
			b = next;
		}
	}

	private List<Long> filterEven(List<Long> sequence) {
		return filter(sequence, true);
	}

	private List<Long> filterOdd(List<Long> sequence) {
		return filter(sequence, false);
	}

	private List<Long> filter(List<Long> sequence, boolean isEven) {
		List<Long> filtered = new ArrayList<>();
		for (Long num : sequence) {
			if ((num % 2 == 0) == isEven) {
				filtered.add(num);
			}
		}
		return filtered;
	}

	private List<Long> filterByMultiple(List<Long> sequence, int n) {
		List<Long> filtered = new ArrayList<>();
		for (Long num : sequence) {
			if (num % n == 0) {
				filtered.add(num);
			}
		}
		return filtered;
	}

	private void showBucketSortWindow(List<Long> sequence) {
		buckets.clear();
		for (int i = 0; i < 9; i++) {
			buckets.add(new ArrayList<>());
		}

		// Place each number into the appropriate bucket based on division
		for (Long num : sequence) {
			boolean placed = false;
			for (int i = 9; i >= 2; i--) {
				if (num % i == 0) {
					buckets.get(9 - i).add(num);
					placed = true;
					break;
				}
			}
			if (!placed) {
				buckets.get(8).add(num); // Non-divisible bucket
			}
		}

		// Sort buckets based on the order
		for (List<Long> bucket : buckets) {
			if (isDescendingSort) {
				bucket.sort((a, b) -> Long.compare(b, a)); // Sort descending
			} else {
				bucket.sort(Long::compareTo); // Sort ascending
			}
		}

		// Create a new window to display the buckets
		Stage bucketStage = new Stage();
		VBox bucketLayout = new VBox(10);
		for (int i = 0; i < buckets.size(); i++) {
			Label bucketLabel = new Label("Bucket " + (9 - i) + ": " + buckets.get(i));
			bucketLayout.getChildren().add(bucketLabel);
		}

		Scene bucketScene = new Scene(bucketLayout, 400, 300);
		bucketStage.setScene(bucketScene);
		bucketStage.setTitle("Bucket Sort Results");
		bucketStage.show();
	}

	private String searchInBuckets(long searchNumber) {
		for (int i = 0; i < buckets.size(); i++) {
			List<Long> bucket = buckets.get(i);
			for (int j = 0; j < bucket.size(); j++) {
				if (bucket.get(j).equals(searchNumber)) {
					return searchNumber + " is in bucket " + (9 - i) + " at position " + (j + 1);
				}
			}
		}
		return "Number not found!";
	}

	private void saveToFile(String data) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Fibonacci Sequence");
		File file = fileChooser.showSaveDialog(null);
		if (file != null) {
			try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
				bos.write(data.getBytes());
				showAlert("Data saved successfully.");
			} catch (IOException e) {
				showAlert("Error saving file: " + e.getMessage());
			}
		}
	}

	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
