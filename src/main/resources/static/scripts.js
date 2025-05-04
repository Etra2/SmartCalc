// ======== KALKULATOR KLASYCZNY ========

// Dodaje znak do pola wyrażenia
function addDigit(char) {
    const input = document.getElementById('expression');
    input.value += char;
}

// Czyści pole wejściowe
function clearInput() {
    const input = document.getElementById('expression');
    input.value = '';
}

// Obsługa zdarzeń po załadowaniu strony
document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('expression');

    document.addEventListener('keydown', (event) => {
        const isMatrixField = event.target.closest('.matrix');  // Sprawdzamy, czy zdarzenie dotyczy pola macierzy

        if (isMatrixField) return;  // Nie blokujemy wejść w polach macierzy

        const allowedKeys = '0123456789+-*/().%';

        if (allowedKeys.includes(event.key)) {
            event.preventDefault();
            if (event.key === '.' && !input.value.includes('.')) {
                input.value += event.key;
            } else if (event.key !== '.') {
                input.value += event.key;
            }
        }

        if (event.key === ',') {
            event.preventDefault();
            input.value += '.';
        }

        if (event.key === 'Enter') {
            event.preventDefault();
            document.querySelector('.calculator-form').submit();
        }

        if (event.key === 'Backspace') {
            event.preventDefault();
            input.value = input.value.slice(0, -1);
        }

        if (event.key === 'Escape') {
            event.preventDefault();
            clearInput();
        }
    });

    const scientificBtn = document.getElementById('scientificCalcTab');
    if (scientificBtn) {
        scientificBtn.addEventListener('click', () => {
            window.location.href = '/scientific';
        });
    }
});

// ======== KALKULATOR MACIERZY ========

// Generuje dynamiczną macierz A lub B
function generateMatrix(id) {
    const rows = document.getElementById(`rows${id}`).value;
    const cols = document.getElementById(`cols${id}`).value;
    const container = document.getElementById(`matrix${id}Container`);

    container.innerHTML = `<h4>Macierz ${id}</h4>`;
    const table = document.createElement("table");
    table.classList.add("matrix");

    for (let i = 0; i < rows; i++) {
        const tr = document.createElement("tr");
        for (let j = 0; j < cols; j++) {
            const td = document.createElement("td");
            const input = document.createElement("input");
            input.type = "number";
            input.name = `${id}[${i}][${j}]`;
            input.value = 0;  // Domyślna wartość 0
            input.setAttribute('inputmode', 'numeric');  // Umożliwia wprowadzanie liczb na urządzeniach mobilnych
            td.appendChild(input);
            tr.appendChild(td);
        }
        table.appendChild(tr);
    }

    container.appendChild(table);
}

// Odczytuje dane macierzy z inputów
// Odczytuje dane macierzy z inputów
function readMatrixFromContainer(containerId) {
    const container = document.getElementById(containerId);
    const inputs = container.getElementsByTagName('input');

    // Wyciągamy literę A lub B z ID kontenera
    const id = containerId.includes("A") ? "A" : "B";

    const rows = parseInt(document.getElementById(`rows${id}`).value);
    const cols = parseInt(document.getElementById(`cols${id}`).value);

    const matrix = [];

    for (let i = 0; i < rows; i++) {
        const row = [];
        for (let j = 0; j < cols; j++) {
            const inputName = `${id}[${i}][${j}]`;
            const input = [...inputs].find(inp => inp.name === inputName);
            // Odczytujemy wartość z pola input
            row.push(parseFloat(input?.value) || 0);  // Domyślnie 0, jeśli wartość jest pusta
        }
        matrix.push(row);
    }

    return matrix;
}

// Wysyła dane do backendu i obsługuje wynik
function performOperation(type) {
    const matrixA = readMatrixFromContainer('matrixAContainer');
    const matrixB = readMatrixFromContainer('matrixBContainer');

    fetch(`/api/matrix/${type}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ matrixA, matrixB })
    })
        .then(res => {
            if (!res.ok) {
                return res.text().then(text => { throw new Error(text); });
            }
            return res.json();
        })
        .then(result => {
            renderMatrixResult(result);
        })
        .catch(error => {
            document.getElementById("resultContainer").innerText = "Błąd: " + error.message;
        });
}

// Wyświetla wynikową macierz w HTML
function renderMatrixResult(result) {
    const resultContainer = document.getElementById("resultContainer");
    resultContainer.innerHTML = "<h4>Wynik:</h4>";

    const table = document.createElement("table");
    table.classList.add("matrix");

    for (let i = 0; i < result.length; i++) {
        const tr = document.createElement("tr");
        for (let j = 0; j < result[i].length; j++) {
            const td = document.createElement("td");
            td.textContent = result[i][j].toFixed(2);
            tr.appendChild(td);
        }
        table.appendChild(tr);
    }

    resultContainer.appendChild(table);
}
