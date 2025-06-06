// Nasłuchuj zdarzenia submit na formularzu o id 'ai-form'
document.getElementById('ai-form').addEventListener('submit', async (e) => {
    e.preventDefault(); // zapobiega domyślnemu przeładowaniu strony po submit

    // Pobierz wartość z textarea (treść zadania) i selecta (wybrana opcja)
    const task = document.getElementById('task-input').value.trim();
    const option = document.getElementById('option-select').value;

    // Sprawdź, czy textarea nie jest pusta
    if (!task) {
        alert('Proszę wpisać treść zadania.');
        return; // przerwij dalsze wykonywanie, jeśli puste
    }

    // Pobierz div na wynik i ustaw komunikat ładowania
    const resultDiv = document.getElementById('ai-result');
    resultDiv.textContent = 'Ładowanie...';

    try {
        // Wyślij dane do backendu (endpoint /ai/solve) metodą POST w formacie JSON
        const response = await fetch('/ai/solve', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ task, option })
        });

        // Jeśli odpowiedź nie jest OK (status poza 200-299), zgłoś błąd
        if (!response.ok) {
            throw new Error('Błąd połączenia z serwerem');
        }

        // Odczytaj odpowiedź jako tekst (może to być np. wynik z AI)
        const resultText = await response.text();

        // Wyświetl wynik w elemencie #ai-result
        resultDiv.textContent = resultText;
    } catch (error) {
        // W przypadku błędu wyświetl komunikat z informacją o błędzie
        resultDiv.textContent = 'Wystąpił błąd: ' + error.message;
    }
});

// Opcjonalna funkcja do ręcznego wywołania rozwiązania AI (np. z innego miejsca)
// Wysyła te same dane i wyświetla odpowiedź w elemencie #ai-result
async function solveWithAI(task, option) {
    const response = await fetch('/ai/solve', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ task, option })
    });

    if (!response.ok) {
        alert('Błąd podczas łączenia z AI.');
        return;
    }

    const data = await response.text(); // Odczyt odpowiedzi jako tekst
    document.getElementById('ai-result').textContent = data;
}
