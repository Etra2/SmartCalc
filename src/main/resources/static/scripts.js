// ======== KALKULATOR KLASYCZNY ========

// Dodaje znak do pola input #expression w miejscu kursora lub na końcu
function addDigit(char) {
    const input = document.getElementById('expression');
    const start = input.selectionStart;
    const end = input.selectionEnd;
    const text = input.value;

    // Wstaw znak w miejsce zaznaczenia lub kursora
    input.value = text.slice(0, start) + char + text.slice(end);

    // Przesuń kursor za wstawiony znak
    const newPos = start + char.length;
    input.selectionStart = input.selectionEnd = newPos;

    input.focus();
}

// Czyści pole input
function clearInput() {
    document.getElementById('expression').value = '';
}

// Wstawia symbol (np. logiczny) do pola input w miejscu kursora
function insertToExpression(symbol) {
    addDigit(symbol);
}

// Obsługa klawiatury w polu #expression
document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('expression');
    if (!input) return;

    input.addEventListener('keydown', (event) => {
        const allowedKeys = '0123456789+-*/().%PQRTSUVWpqrsuvwxyz∧∨→↔¬01';

        // Zamiana przecinka na kropkę
        if (event.key === ',') {
            event.preventDefault();
            if (!input.value.includes('.')) {
                addDigit('.');
            }
            return;
        }

        // Pozwalaj na dozwolone znaki
        if (allowedKeys.includes(event.key)) {
            return;
        }

        // Obsługa Backspace, Delete, strzałek, Tab
        if (['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'].includes(event.key)) return;

        // Enter - wyślij formularz (jeśli istnieje)
        if (event.key === 'Enter') {
            event.preventDefault();
            document.querySelector('.calculator-form')?.submit();
            return;
        }

        // Escape - czyść pole
        if (event.key === 'Escape') {
            event.preventDefault();
            clearInput();
            return;
        }

        // Blokuj inne znaki
        event.preventDefault();
    });

    // Obsługa przycisków logiki matematycznej (np. ∧, ∨)
    const logicButtons = document.querySelectorAll('.logic-button');
    logicButtons.forEach(button => {
        button.addEventListener('click', () => {
            insertToExpression(button.dataset.value);
        });
    });

    // Obsługa przycisku przełączania na kalkulator naukowy (jeśli jest)
    const scientificBtn = document.getElementById('scientificCalcTab');
    if (scientificBtn) {
        scientificBtn.addEventListener('click', () => {
            window.location.href = '/scientific';
        });
    }
});

// ======== KALKULATOR MACIERZY ========

// Generuje macierz o podanych wymiarach w kontenerze
function generateMatrix(id) {
    const rows = parseInt(document.getElementById(`rows${id}`)?.value) || 0;
    const cols = parseInt(document.getElementById(`cols${id}`)?.value) || 0;
    const container = document.getElementById(`matrix${id}Container`);

    if (!container) return;

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
            input.value = 0;
            input.setAttribute('inputmode', 'numeric');
            td.appendChild(input);
            tr.appendChild(td);
        }
        table.appendChild(tr);
    }
    container.appendChild(table);
}

// Odczytuje macierz z pola input w danym kontenerze
function readMatrixFromContainer(containerId) {
    const container = document.getElementById(containerId);
    if (!container) return [];

    const inputs = container.getElementsByTagName('input');
    const id = containerId.includes("A") ? "A" : "B";
    const rows = parseInt(document.getElementById(`rows${id}`)?.value) || 0;
    const cols = parseInt(document.getElementById(`cols${id}`)?.value) || 0;

    const matrix = [];
    for (let i = 0; i < rows; i++) {
        const row = [];
        for (let j = 0; j < cols; j++) {
            const inputName = `${id}[${i}][${j}]`;
            const input = [...inputs].find(inp => inp.name === inputName);
            row.push(parseFloat(input?.value) || 0);
        }
        matrix.push(row);
    }
    return matrix;
}

// Wysyła żądanie operacji na macierzach i wyświetla wynik
function performOperation(type) {
    const matrixA = readMatrixFromContainer('matrixAContainer');
    const matrixB = readMatrixFromContainer('matrixBContainer');
    const numberValue = parseFloat(document.getElementById('numberInput')?.value || '0');

    let requestBody = { matrixA, matrixB };
    let url = `/api/matrix/${type}`;

    if (type === 'multiplyScalar') {
        url += `?scalar=${numberValue}`;
    } else if (type === 'power') {
        requestBody.exponent = parseInt(numberValue);
    }

    fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestBody)
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

// Renderuje wynik macierzowy w tabeli
function renderMatrixResult(result) {
    const resultContainer = document.getElementById("resultContainer");
    if (!resultContainer) return;

    resultContainer.innerHTML = "<h4>Wynik:</h4>";

    if (!Array.isArray(result) || result.length === 0 || !Array.isArray(result[0])) {
        resultContainer.innerHTML += `<p>${result}</p>`;
        return;
    }

    const table = document.createElement("table");
    table.classList.add("matrix");

    for (const row of result) {
        const tr = document.createElement("tr");
        for (const val of row) {
            const td = document.createElement("td");
            td.textContent = Number.isFinite(val) ? val.toFixed(2) : val;
            tr.appendChild(td);
        }
        table.appendChild(tr);
    }

    resultContainer.appendChild(table);
}

// ======== KALKULATOR LOGIKI MATEMATYCZNEJ ========

// Poprawiona obsługa formularza logiki matematycznej z dodanym zabezpieczeniem, komentarzami i lepszą obsługą błędów
document.addEventListener('DOMContentLoaded', () => {
    const logicForm = document.getElementById("logic-form");
    if (!logicForm) return; // Zabezpieczenie: jeśli formularz nie istnieje, nie wykonuj dalej

    logicForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const expr = document.getElementById("expression").value.trim();

        if (!expr) {
            alert("Wprowadź wyrażenie logiczne.");
            return;
        }

        console.log("Wysyłane wyrażenie:", expr);

        fetch('/logic/evaluate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ expression: expr })
        })
            .then(res => {
                if (!res.ok) {
                    // Pobieranie treści błędu z odpowiedzi serwera
                    return res.text().then(text => { throw new Error(text || "Błąd odpowiedzi serwera."); });
                }
                return res.json();
            })
            .then(data => {
                if (!data.variables || !data.truthTable) {
                    throw new Error("Błędny format odpowiedzi z serwera.");
                }
                renderLogicTable(data);
            })
            .catch(err => {
                console.error("Błąd:", err);
                alert("Wystąpił błąd przy przetwarzaniu wyrażenia. Sprawdź składnię.");
            });
    });
});

// Poprawiona funkcja renderująca tabelę prawdy z zabezpieczeniami i komentarzami
function renderLogicTable(data) {
    const headerRow = document.getElementById("header-row");
    const tableBody = document.getElementById("table-body");

    if (!headerRow || !tableBody) return; // Sprawdzenie istnienia elementów tabeli

    headerRow.innerHTML = "";
    tableBody.innerHTML = "";

    // Nagłówki zmiennych
    data.variables.forEach(variable => {
        const th = document.createElement("th");
        th.textContent = variable;
        headerRow.appendChild(th);
    });

    // Nagłówek kolumny wyniku
    const resultHeader = document.createElement("th");
    resultHeader.textContent = "Wynik";  // Upewnij się, że backend zwraca ten klucz
    headerRow.appendChild(resultHeader);

    // Jeśli brak danych w truthTable, wyświetl komunikat
    if (!data.truthTable || data.truthTable.length === 0) {
        const tr = document.createElement("tr");
        const td = document.createElement("td");
        td.setAttribute("colspan", data.variables.length + 1);
        td.textContent = "Brak wyników do wyświetlenia";
        tr.appendChild(td);
        tableBody.appendChild(tr);
        return;
    }

    // Wiersze tabeli z wartościami
    data.truthTable.forEach(row => {
        const tr = document.createElement("tr");

        data.variables.forEach(variable => {
            const td = document.createElement("td");
            td.textContent = row[variable] !== undefined ? row[variable] : "-";
            tr.appendChild(td);
        });

        const resultTd = document.createElement("td");
        resultTd.textContent = row["Wynik"] !== undefined ? row["Wynik"] : "-";
        tr.appendChild(resultTd);

        tableBody.appendChild(tr);
    });
}