# QuizCrafter AI

[![Demo Video](https://img.shields.io/badge/Demo-Video-blue)](https://drive.google.com/file/d/1JZzrkAbJN-SC7DQA5WviVeFbS-1pcQu4/view?usp=sharing)
[![Live Site](https://img.shields.io/badge/Live%20Site-quizcrafter--ai.web.app-green)](https://quizcrafter-ai.web.app/)

 **QuizCrafter AI** is a web app that allows users to automatically generate quizzes from PDF files using AI. It’s designed to help educators, students, and content creators craft custom quizzes in seconds and evaluate answers intelligently.

---

## 🧠 Features

-  Upload a PDF and specify number of questions
-  Auto-generates quiz questions using an AI-powered backend
-  Check answers with automatic evaluation and scoring
-  Displays score visually with progress bars
-  Summarizes uploaded content into concise bullet points
-  Saves quiz history to Firebase Firestore
-  Shows associated images (if available) from Firebase Storage
-  Authenticated user dashboard with profile support

---

## Tech Stack

- **Frontend:** HTML, CSS, JavaScript (Vanilla + Firebase Auth)
- **Backend:** Java Spring Boot (runs the AI processing + summary logic)
- **Database:** Firebase Firestore
- **Storage:** Firebase Storage (for user-uploaded files and quiz images)
- **Authentication:** Firebase Authentication

---

##  Getting Started

### 🔗 Try it Live
👉 [Visit the Live App](https://quizcrafter-ai.web.app/)

### 📺 Watch the Demo
🎬 [View Demo Video](https://drive.google.com/file/d/1JZzrkAbJN-SC7DQA5WviVeFbS-1pcQu4/view?usp=sharing)

---

##  Project Structure
quizcrafter-ai/ ├── public/ │ ├── index.html │ ├── main.html │ ├── history.html │ └── profile.html ├── src/ │ ├── App.java (Spring Boot app) │ ├── controller/ │ ├── service/ ├── firebase/ │ └── Firestore + Storage rules └── README.md

---

## 💡 Future Improvements

- Support for multiple-choice generation
- User-level analytics dashboard
- Admin panel to manage content
- Export quizzes as PDFs

---

## 👨‍💻 Author

**Mohammed Rakibul Hasan**


