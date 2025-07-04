# 📝 TaskiAssessment - Event Planner Android App

TaskiAssessment is a modern task and event management Android application built using the latest Android Jetpack components and best practices. It allows users to add, edit, delete, and get reminders for upcoming events.

## ✨ Features

- 📆 Add/Edit/Delete events with title, description, date, and time
- 🔔 Alarm reminders using AlarmManager
- 🗓 View events by date or upcoming ones
- 🌓 Full support for Light and Dark themes (Material 3)
- 🧠 MVVM architecture with Room DB and LiveData
- 🔧 Built using modern tools like Hilt, ViewBinding, and Navigation Component

---

## 📸 Screenshots

| Home - Upcoming Events | Add/Edit Event | Events by Date |
|------------------------|----------------|----------------|
| ![Upcoming](screenshots/upcoming.png) | ![Add](screenshots/add.png) | ![Calendar](screenshots/calendar.png) |

---

## 🛠 Tech Stack

| Tool/Library           | Purpose                              |
|------------------------|--------------------------------------|
| Kotlin                 | Language                             |
| Android Jetpack        | Lifecycle, Navigation, ViewModel     |
| Room DB                | Local storage                        |
| Hilt                   | Dependency Injection                 |
| Material 3             | UI Components + Theming              |
| AlarmManager           | Scheduling reminders                 |
| LiveData + ViewModel   | State management                     |
| ConstraintLayout       | Modern, responsive UI layout         |

---

## ⚙️ Architecture

MVVM (Model-View-ViewModel)
│
├── Model: Room DB (Entities, DAO, DB)
├── ViewModel: Business logic & UI state
└── View (Fragments): UI interaction & rendering


---

## 🧪 How It Works

- **Events** are stored in `Room` using an `EventEntity`.
- Alarms are scheduled using `AlarmManager.setExactAndAllowWhileIdle()`.
- A custom `BroadcastReceiver` (`EventReminderReceiver`) shows notifications at the scheduled time.
- Light & Dark themes are defined using Material You styles and custom colors.

---

🔔 Reminder Permission (Android 13+)
If you're targeting Android 13+, you'll need to request SCHEDULE_EXACT_ALARM permission. This is handled internally and will gracefully fall back if not granted.


🧑‍💻 Author
Parithi D B




