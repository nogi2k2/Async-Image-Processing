# Async Image Processor

Async Image Processor is a Java-based application designed to benchmark and demonstrate the performance benefits of **asynchronous, multi-threaded processing** over traditional synchronous execution for CPU-bound tasks.

Built with **JavaFX** for real-time visualization, the system breaks large images into sub-tiles/sub-images and applies pixel-manipulation filters concurrently. It leverages a custom `ExecutorService` and `CompletableFuture` to maximize CPU utilization across all available cores.

> **Benchmark Result:** On a 16-core machine, this application demonstrates speedups of approximately **15x to 19x** for heavy image processing tasks

---

## Features

* **Dual Processing Modes:**
    * **Synchronous:** Processes image tiles sequentially on a single thread (Main Manager Daemon Thread). Visually renders as a linear scan (top-to-bottom)
    * **Asynchronous:** Processes image tiles in parallel using a thread pool optimized for the host machine's core count. Visually renders as a "popcorn" effect (tiles appear randomly as threads complete)
* **Real-Time Visualization:** Uses a non-blocking `AnimationTimer` and `JavaFX Canvas` to render processed tiles immediately upon completion without freezing the UI
* **Strategy Pattern Implementation:** Filters are implemented as interchangeable strategies, allowing for easy extensibility
* **Robust I/O:** Supports robust image reading and writing from any source (Sockets, File)
* **Interactive CLI:** A dedicated daemon thread runs a command-line interface for user input to prevent blocking the graphical interface

---

## Tech Stack

* **Language:** Java 21
* **GUI Framework:** JavaFX (Canvas, AnimationTimer)
* **Concurrency:** `java.util.concurrent` (ExecutorService, CompletableFuture)
* **Build Tool:** Maven
* **Architecture:** MVC-inspired with strict separation of Processing, I/O, and Rendering logic

---

## System Architecture

### Core Components

1.  **ImageProcessor:**
    * The core engine of the application
    * **Async Mode:** Splits the image into **image_width/10 x image_height/10** tiles. Submits each sub-tile as a `CompletableFuture` task to a fixed thread pool (sized to available processor cores)
    * **Sync Mode:** Iterates through the same grid sequentially on the calling thread

2.  **Filters (Strategy Pattern):**
    * `ImageFilter` interface defines the contract
    * Implementations include: `GreyScaleFilter`, `BrightnessFilter`, `ContrastFilter`, `SaturationFilter`, `InvertFilter`, and `BoxBlurFilter` (3x3 convolution)
    * Filters operate on `BufferedImage` objects

3.  **DrawMultipleImagesOnCanvas (Singleton):**
    * Manages the JavaFX `Canvas`
    * Uses a thread-safe `LinkedBlockingQueue` to receive processed `ImageData` objects from worker threads
    * An `AnimationTimer` polls the queue at 60fps and draws tiles to the screen, ensuring thread safety on the JavaFX Application Thread

---

## Project Structure

```
com.image.imageprocessing/
├── App.java                              # Main entry point, CLI menu, and lifecycle management
│
├── filter/                               # Filter Strategy Interface and Implementations
│ ├── ImageFilter.java
│ ├── GreyScaleFilter.java
│ ├── BoxBlurFilter.java
│ ├── BrightnessFilter.java
│ ├── ContrastFilter.java
│ ├── InvertFilter.java
│ └── SaturationFilter.java
│
├── image/
│ ├── DrawMultipleImagesOnCanvas.java     # Singleton Canvas Manager
│ └── ImageData.java                      # DTO holding buffered image and coordinates
│
├── io/
│ ├── ImageReadInf.java                   # I/O Interface
│ └── FileImageIO.java                    # FILE I/O Implementation using ImageIO and Optional
│
└── processor/
└── ImageProcessor.java                   # Logic for Tiling and Thread Pool Management
```

---

## Performance Benchmarks

The following benchmarks were recorded on a machine with **16 Logical Cores**. The test image resolution was **1920x1280**. An artificial delay of 1ms per tile was added to simulate heavy CPU load and visualize thread scheduling

| Filter Type | Mode | Processing Time (ms) | Speedup Factor |
| :--- | :--- | :--- | :--- |
| **Grayscale** | Synchronous | 49,829 ms | N/A |
| | Asynchronous | 3,217 ms | **15.5x** |
| **Box Blur** | Synchronous | 52,008 ms | N/A |
| | Asynchronous | 3,363 ms | **15.4x** |
| **Brightness** | Synchronous | 49,283 ms | N/A |
| | Asynchronous | 3,096 ms | **15.9x** |
| **Contrast** | Synchronous | 47,148 ms | N/A |
| | Asynchronous | 3,107 ms | **15.1x** |
| **Saturation** | Synchronous | 48,357 ms | N/A |
| | Asynchronous | 3,087 ms | **15.6x** |
| **Invert** | Synchronous | 61,420 ms | N/A |
| | Asynchronous | 3,168 ms | **19.3x** |

**Analysis:** The Asynchronous mode consistently outperforms the Synchronous mode by a factor closely matching the number of available cores (16). This demonstrates near-perfect scaling for parallelizable CPU-bound tasks

---

## Installation and Usage

### Prerequisites

* Java JDK 17 or higher
* Maven 3.6 or higher

### Setup

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/<user-name>/Async-Image-Processing.git
    cd async-image-processor
    ```

2.  **Prepare Input Directory:**
    Ensure your source images are located in the configured input path (default: `src/main/inputs/`)

3.  **Compile and Run:**
    ```bash
    mvn clean compile
    mvn javafx:run
    ```

### Usage Workflow

1.  **Image Selection:** Upon startup, the terminal will prompt for the filename (e.g., `image-name.jpg`)
2.  **Select Mode:** Choose between Synchronous (1) or Asynchronous (2)
3.  **Select Filter:** Choose a filter (e.g., Grayscale, Box Blur) and provide parameters if requested (e.g., brightness level, contrast factor, saturation factor)
4.  **View Results:**
    * Watch the JavaFX window to see the rendering process in real-time
    * Check the terminal for total execution time
    * The final processed image is automatically saved to `src/main/outputs/` with a descriptive filename (e.g., `sea_Async_BoxBlurFilter.jpg`)

---
