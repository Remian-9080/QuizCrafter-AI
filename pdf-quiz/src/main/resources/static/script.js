async function uploadPDF() {
  const file = document.getElementById("pdfUpload").files[0];
  const numQuestions = document.getElementById("numQuestions").value || 5;

  if (!file) return alert("Please select a PDF.");

  const formData = new FormData();
  formData.append("file", file);
  formData.append("numQuestions", numQuestions);

  const res = await fetch("http://localhost:8080/upload", {
    method: "POST",
    body: formData
  });

  const data = await res.json();
  if (!res.ok) return alert(data.error || "Upload failed");

  const questions = data.quizGeneratedByLLM.trim().split("\n");
  const container = document.getElementById("questionsContainer");
  container.innerHTML = "";

  questions.forEach((q, i) => {
    container.innerHTML += `
      <div class="section">
        <label><strong>Q${i + 1}:</strong> ${q}</label>
        <textarea id="answer${i}" rows="2" style="width: 100%; margin-top: 5px;"></textarea>
      </div>
    `;
  });
}

async function generateSummary() {
  const res = await fetch("http://localhost:8080/summary", {
    method: "POST"
  });

  const data = await res.json();
  document.getElementById("summaryOutput").textContent = data.summary || data.error;
}

async function checkAnswers() {
  const sections = document.querySelectorAll("#questionsContainer .section");
  const questions = [], answers = [];

  sections.forEach((section, i) => {
    const label = section.querySelector("label").textContent;
    const answer = section.querySelector("textarea").value;
    questions.push(label);
    answers.push(answer);
  });

  const res = await fetch("http://localhost:8080/check-answers", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      questions: questions.join("\n"),
      answers: answers.join("\n")
    })
  });

  const data = await res.json();
  document.getElementById("evaluationOutput").textContent = data.evaluation || data.error;
}

window.addEventListener('DOMContentLoaded', () => {
  firebase.auth().onAuthStateChanged(user => {
    if (user) {
      const photoURL = user.photoURL;
      const profilePic = document.getElementById("profilePic");
      if (photoURL && profilePic) {
        profilePic.src = photoURL;
      }
    }
  });
});
