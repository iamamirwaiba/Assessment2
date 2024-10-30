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

public class Assessment2i1 extends Application {

	private List<Long> fibonacciSequence = new ArrayList<>();

	@Override
	public void start(Stage primaryStage) {
		TextField lengthInput = new TextField();
		lengthInput.setPromptText("Enter length");
		List<Long> originalSequence = new ArrayList<>();

		Button fibGenButton = new Button("Fib Gen");
		TextArea outputArea = new TextArea();
		outputArea.setEditable(false);

		Button filterEvenButton = new Button("Filter Even");
		Button filterOddButton = new Button("Filter Odd");
		Button saveButton = new Button("Save to File");
		Button bucketSortButton = new Button("Bucket Sort");
		CheckBox orderCheckBox = new CheckBox("Ascending Order");

		TextField multipleInput = new TextField();
		multipleInput.setPromptText("Enter multiple n");
		Button filterMultipleButton = new Button("Filter by Multiple of n");

		fibGenButton.setOnAction(e -> {
			int length = Integer.parseInt(lengthInput.getText());
			if (length >= 0) {
				generateFibonacci(length);
				outputArea.setText(fibonacciSequence.toString());
				originalSequence.addAll(fibonacciSequence);
			} else {
				showAlert("Please enter a length that is not Negative");
			}
		});

		filterEvenButton.setOnAction(e -> {
			if(!orderCheckBox.isSelected()) {
				if (outputArea.getText().equals(originalSequence.toString())) {
					List<Long> filtered = filterEven(fibonacciSequence);
					filtered.sort( Comparator.reverseOrder() );
					outputArea.setText(filtered.toString());
				} else {
					outputArea.setText(originalSequence.toString());
				}
			}
			else {
				if (outputArea.getText().equals(originalSequence.toString())) {
					List<Long> filtered = filterEven(fibonacciSequence);
					outputArea.setText(filtered.toString());
				} else {
					outputArea.setText(originalSequence.toString());
				}
			}
		});

		filterOddButton.setOnAction(e -> {
			if(!orderCheckBox.isSelected()) {
				if (outputArea.getText().equals(originalSequence.toString())) {
					List<Long> filtered = filterOdd(fibonacciSequence);
					filtered.sort( Comparator.reverseOrder() );
					outputArea.setText(filtered.toString());
				} else {
					outputArea.setText(originalSequence.toString());
				}
			}
			else {
				if ( outputArea.getText( ).equals( originalSequence.toString( ) ) ) {
					List< Long > filtered = filterOdd( fibonacciSequence );
					outputArea.setText( filtered.toString( ) );
				} else {
					outputArea.setText( originalSequence.toString( ) );
				}
			}
		});

		filterMultipleButton.setOnAction(e -> {
			try {
				if(fibonacciSequence.size() > 1) {
					if(!orderCheckBox.isSelected()) {
						int n = Integer.parseInt( multipleInput.getText( ) );
						List< Long > filtered = filterByMultiple( fibonacciSequence, n );
						filtered.sort( Comparator.reverseOrder() );
						outputArea.setText( filtered.toString( ) );
					}
					else {
						int n = Integer.parseInt( multipleInput.getText( ) );
						List< Long > filtered = filterByMultiple( fibonacciSequence, n );
						outputArea.setText( filtered.toString( ) );
					}
				}
				else{
					outputArea.setText("Please Generate the series first");
				}
			} catch (NumberFormatException ex) {
				showAlert("Please enter a valid integer for n.");
			}
		});

		bucketSortButton.setOnAction(e -> {
			showBucketSortWindow(fibonacciSequence,orderCheckBox.isSelected());
		});


		saveButton.setOnAction(e -> saveToFile(outputArea.getText()));

		VBox layout = new VBox(10, lengthInput, fibGenButton, outputArea, orderCheckBox, filterEvenButton, filterOddButton,
				multipleInput, filterMultipleButton, bucketSortButton, saveButton);
		Scene scene = new Scene(layout, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Fibonacci Generator");
		primaryStage.show();
	}

	private void showBucketSortWindow(List<Long> sequence,Boolean order) {
		List<List<Long>> buckets = new ArrayList<>();
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

		// Create a new window to display the buckets
		Stage bucketStage = new Stage();
		VBox bucketLayout = new VBox(10);
		for (int i = 0; i < buckets.size(); i++) {
			if(!order){
				buckets.get(i).sort( Comparator.reverseOrder() );
				Label bucketLabel = new Label("Bucket " + (9 - i) + ": " + buckets.get(i));
				bucketLayout.getChildren().add(bucketLabel);
			}
			else {
				Label bucketLabel = new Label( "Bucket " + ( 9 - i ) + ": " + buckets.get( i ));
				bucketLayout.getChildren( ).add( bucketLabel );
			}

		}

		Scene bucketScene = new Scene(bucketLayout, 400, 300);

		bucketStage.setScene(bucketScene);
		bucketStage.setTitle("Bucket Sort Results");
		bucketStage.show();
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
		List<Long> filtered = new ArrayList<>();
		for (Long num : sequence) {
			if (num % 2 == 0) {
				filtered.add(num);
			}
		}
		return filtered;
	}

	private List<Long> filterOdd(List<Long> sequence) {
		List<Long> filtered = new ArrayList<>();
		for (Long num : sequence) {
			if (num % 2 != 0) {
				filtered.add(num);
			}
		}
		return filtered;
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
