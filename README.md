<h1 align="center">WeatherTracker</h1>

### 📝 Main Technologies

|  **Database**  |                                                     [![Postgresql](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)                                                      |
|:--------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|  **Backend**   | [![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)](https://dev.java/) [![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/) |
|  **Frontend**  |                                                                     [![HTML](https://img.shields.io/badge/Thymeleaf-008000?style=for-the-badge&logo=Thymeleaf)](https://www.thymeleaf.org)                                                                     |
| **Build Tool** |                                                             [![Maven](https://img.shields.io/badge/Maven-orange?style=for-the-badge&logo=ApacheMaven&logoColor=FF00FF)](https://maven.apache.org)                                                              |
|    **PaaS**    |                                                            [![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)                                                             |

---

## ⚙️ Getting Started

### 🐋 Start with Docker

* Clone the repository

```console
https://github.com/Encerel/WeatherTracker
```

* Use docker-compose

```console
docker-compose build
```

```console
docker-compose up
```

## Application Features

### 1.1 User related

> Classic authorization

- Sign in
- Sign up
- Sign out

### 1.2 Locations related

> Classic CRUD

- **Search** a location to track the weather
- **Add** a location to the tracked list
- **View** a list of locations with weather for each location
- **Delete** a location from the tracked list

### 2.2 Data Model

The application have three main entities - User, Session and Location.

![Tools diagram](img/Db-diagram.png)

## 🪄 Demo

![](img/sign_in_page.png)

![](img/locations_by_name.png)

![](img/tracked_weather.png)





