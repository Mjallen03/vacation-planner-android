# Vacation Planner App
### Western Governors University – D308 Mobile Application Development (Android)

---

## Overview
## Title and purpose
**Vacation Planner** is an Android mobile application that allows travelers to organize vacations and associated excursions.  
The app supports adding, editing, deleting, and viewing vacations and excursions using the **Room Framework** as an abstraction layer over **SQLite**.  
Users receive alerts for vacation start and end dates, as well as excursion dates, and can share vacation details via built-in Android sharing options.

---

## Environment Setup
- **IDE:** Android Studio Giraffe | 2022.3.1 Patch 3
- **Language:** Java 17
- **Gradle:** 8.2
- **Target SDK:** Android 13 (API 33 – Tiramisu)
- **Minimum SDK:** Android 8.0 (API 26)
- **Emulator Tested:** Medium Phone API 36.1

All SDK tools were verified by building and running the default “Hello World” project before development.

---

## Database Architecture (Room Framework)
Entities and DAOs:
- **Vacation** → title, hotel, location, start date, end date
- **Excursion** → title, date, description, price, vacation ID (foreign key)

Repositories handle CRUD operations on background threads to keep the UI responsive.

---

## Functional Features

### 1. Vacation Management
- Add, update, delete, and list vacations using **VacationDao** and **VacationRepository**.
- Validation prevents deletion if excursions exist for that vacation.

### 2. Vacation Details
Each vacation stores:
- **Title**, **Hotel/Location**, **Start Date**, **End Date**
- Input validated for correct date format (`MM/dd/yyyy`) and logical order (end after start).

### 3. Vacation Feature Enhancements
- **Detailed View:** Displays all vacation fields.
- **Alerts:** `AlarmManager` + `BroadcastReceiver` trigger start / end date reminders:
  - *“Vacation ‘Hawaii Trip’ is starting today!”*
- **Share Vacation:** Android Intent shares details via email, SMS, or clipboard.
- **List Vacations:** Toast-based quick list of saved vacations.

### 4. Excursion Details
Each excursion includes:
- **Title**, **Date**, **Description**, **Price**, linked to its Vacation ID.
- Managed via **ExcursionActivity** and stored through **ExcursionDao**.

### 5. Excursion Management & Validation
- **Add / Edit / Delete Excursion** with full input validation.
- **Alerts:** Excursion date triggers a Toast reminder.
- **Validation:** Excursion date must fall within its vacation’s start and end dates; otherwise user is warned.

---

## GUI Layouts (Task C)
| Screen | Purpose |
|--------|----------|
| **Home Screen (VacationActivity)** | Central navigation for all vacation & excursion functions |
| **Vacation List** | Toast list of saved vacations |
| **Vacation Detail View** | Displays full vacation info (title, hotel, dates) |
| **Excursion List View (ExcursionListActivity)** | Lists excursions linked to each vacation |
| **Manage Excursions (ExcursionActivity)** | Add / update / delete excursions |

---

## Storyboard (Task D)
The storyboard (`Vacation_Planner_Storyboard.pdf`) visually illustrates navigation between:
Home - Vacation List - Vacation Detail - Excursion List - Manage Excursions.  
Arrows show transitions and button actions, matching the live app flow.

---

## Deployment (Task E)
A signed APK (`app-release.apk`) was generated through:
> **Build - Generate Signed Bundle / APK - APK - Release**  
The keystore and alias were configured, and the build completed successfully.  
The APK was installed on an emulator and verified to perform all required features.

---

## Repository (Task A)
GitLab repository URL:  
[https://gitlab.com/wgu-gitlab-environment/student-repos/mallen41/d308-mobile-application-development-android.git](https://gitlab.com/wgu-gitlab-environment/student-repos/mallen41/d308-mobile-application-development-android.git)

Commit history shows incremental pushes for each rubric task (B1 – B5).

---

## Directions for Operating the Application
The Vacation Planner app is designed to be simple, functional, and fully testable for all rubric aspects (B1–B5 and C). Follow the steps below to demonstrate each feature.

---

### 1. Launching the App (Home Screen – Task C1)
- Open the app on an Android device or emulator (Android 8.0+).
- The **Home Screen (VacationActivity)** appears automatically.
- From here, you can:
  - Add, update, or delete vacations.
  - List all saved vacations.
  - Share vacation details.
  - View and manage excursions.

---

### 2. Adding a Vacation (Task B1–B2)
1. Tap **“Add Vacation.”**
2. Enter:
  - Vacation Title (e.g., *Hawaii Getaway*)
  - Hotel/Location (e.g., *Hilton Resort*)
  - Start Date (MM/dd/yyyy)
  - End Date (MM/dd/yyyy)
3. Tap **Save** to add it to the Room database.
4. If the date format is incorrect, you’ll see:
   > “Dates must be in MM/dd/yyyy format.”
5. If the end date is before the start date, you’ll see:
   > “End date must be after start date.”

This fulfills validation and vacation entry requirements (B1–B3a–B3d).

---

### 3. Viewing Vacation Details and Lists (Task B3a–B3b–C2–C4)
- Tap **“List Vacations”** to see all saved vacations in a Toast message.
- Tap **“Vacation Details”** to view detailed vacation information including:
  - Vacation name, hotel, location, start date, and end date.

This demonstrates the detailed vacation view and list functions required for rubric sections C2 and C4.

---

### 4. Vacation Alerts (Task B3e)
- After adding a vacation, alerts are automatically scheduled using Android’s AlarmManager.
- On the start or end date, a Toast notification appears:
  > “Vacation ‘Hawaii Getaway’ is starting today!”  
  > “Vacation ‘Hawaii Getaway’ is ending today!”

This verifies functioning start and end alerts.

---

### 5. Sharing Vacation Information (Task B3f)
- On the Home Screen, tap **“Share Vacation.”**
- The system’s share chooser appears prefilled with the vacation’s title, hotel, and dates.
- Choose Email, Messages, or another app to send.

This fulfills the “sharing feature” rubric requirement.

---

### 6. Managing Excursions (Task B3g–B3h–B4–B5–C3–C5)
1. Tap **“Manage Excursions”** on the Home Screen.
2. Enter the associated **Vacation ID** (or select vacation first).
3. Use the following buttons:
  - **Add Excursion** - create new excursion record.
  - **Update Excursion** - modify existing record.
  - **Delete Excursion** - remove an excursion.
  - **List Excursions** - view all stored excursions.

Each excursion includes title, date, description, and price.  
All fields are validated before saving.

---

### 7. Excursion Date Validation and Alerts (Task B5c–B5d–B5e)
- Dates must be entered in **MM/dd/yyyy** format.
  - Invalid format - Toast:
    > “Date must be in MM/dd/yyyy format.”
- The excursion date must fall **within** its parent vacation’s dates.
  - If not - Toast:
    > “Excursion date must fall within the vacation period.”
- On the excursion date, an automatic alert displays:
  > “Excursion ‘Snorkeling Trip’ is happening today!”

---

### 8. Viewing Excursion Lists (Task C3–C5)
- Tap **“View Excursions”** to display all excursions associated with each vacation.
- Excursions appear in a scrollable list showing:
  - Vacation Name or ID
  - Excursion Title
  - Date
  - Price
  - Description (if available)

---

## Included Submission Files
- `Vacation_Planner_Storyboard.pdf` – storyboard (Task D)
- `GitLab Repository D308 Task_1.pdf` – commit history (Task A)
- `Task_E_Signed_APK.pdf` – signed APK generation (Task E)
- `app-release.apk` – runnable application

