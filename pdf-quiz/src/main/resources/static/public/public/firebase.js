const firebaseConfig = {
  apiKey: "AIzaSyCLiXp4zwS3y3yLlCXPJEYiWzmIBOCpSQ0",
  authDomain: "quizcrafter-ai.firebaseapp.com",
  projectId: "quizcrafter-ai",
  storageBucket: "quizcrafter-ai.appspot.com", // ✅ Fixed
  messagingSenderId: "585560085435",
  appId: "1:585560085435:web:47c3de8853451527afaa9e",
  measurementId: "G-BM1DP2XESZ"
};

firebase.initializeApp(firebaseConfig);

const auth = firebase.auth();
const db = firebase.firestore();

window.auth = auth;
window.db = db;
