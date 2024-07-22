# Automation API Testing Framework

This repository contains an Automation API Testing Framework built with Kotlin and Maven. The framework is designed to provide a robust, scalable, and maintainable structure for automated testing of RESTful APIs.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Prerequisites for Testing](#prerequisites-for-testing)
- [Running Tests](#running-tests)
- [Reporting](#reporting)
- [Contributing](#contributing)
- [License](#license)

## Prerequisites
Before you begin, ensure you have met the following requirements:
- You have installed [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) or later.
- You have installed [Maven](https://maven.apache.org/install.html).
- You have installed [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).
- IDE capable of running Kotlin. eg. Intellij, Eclipse (requires kotlin plugin), VS Studio (requires kotlin plugin) [although it is possible to run from the command line]

## Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/your-username/automation-api-testing-framework.git
    ```
2. Navigate to the project directory:
    ```sh
    cd automation-api-testing-framework
    ```
3. Install the dependencies:
    ```sh
    mvn -DskipTests=true package
    ```

## Project Structure
The framework follows a standard Maven project structure:
```
automation-api-testing-framework
├── src
│   ├── main
│   │   └── kotlin
│   │       └── org
│   │           └── example
│   │               ├── api
│   │               ├── model
│   │               ├── storage
│   ├── test
│   │   └── kotlin
│   │       └── org
│   │           └── example
│   │               ├── utils
│   │               ├── ...
├── pom.xml
└── README.md
```

- `api`: Api which is to be tested.
- `model`: data classes of the responses and requests for api.
- `storage`: caching class for storing api data.
- `utils`: Helper functions and utilities.

## Configuration
Configuration files are located in the `src/main/kotlin/com/yourpackage/config` directory. Modify the configuration files as per your testing environment.

## Prerequisites for Testing
### Create a .env File
In the root directory of your project, create a file named .env. This file will hold all your environment-specific variables. For example:
```env
API_USERNAME={username-here}
API_PASSWORD={password-here}
```

## Running Tests
To run the tests, use the following Maven command:
```sh
mvn test
```

## Reporting
Test output reports are generated in the `app-info.html` directory by default. 

## Contributing
Contributions are welcome! Please follow these steps to contribute:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add some feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Open a pull request.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE.md) file for details.

---

Thank you for using the Automation API Testing Framework! If you have any questions, feel free to open an issue or contact the project maintainers.