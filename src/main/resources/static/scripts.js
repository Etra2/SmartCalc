// Funkcja obsługująca przyciski na ekranie
function addDigit(char) {
    const input = document.getElementById('expression');
    input.value += char;
}

// Funkcja czyszcząca pole wejściowe
function clearInput() {
    const input = document.getElementById('expression');
    input.value = '';
}

// Całość uruchamiana po załadowaniu strony
document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('expression');

    // Obsługuje klawisze fizyczne
    document.addEventListener('keydown', (event) => {
        const allowedKeys = '0123456789+-*/().%';

        // Dozwolone znaki
        if (allowedKeys.includes(event.key)) {
            event.preventDefault();

            if (event.key === '.' && !input.value.includes('.')) {
                input.value += event.key;
            } else if (event.key !== '.') {
                input.value += event.key;
            }
        }

        // Zamiana przecinka na kropkę
        if (event.key === ',') {
            event.preventDefault();
            input.value += '.';
        }

        // Enter = oblicz
        if (event.key === 'Enter') {
            event.preventDefault();
            document.querySelector('.calculator-form').submit();
        }

        // Backspace = usuń ostatni znak
        if (event.key === 'Backspace') {
            event.preventDefault();
            input.value = input.value.slice(0, -1);
        }

        // Escape = wyczyść
        if (event.key === 'Escape') {
            event.preventDefault();
            clearInput();
        }
    });

    // Przycisk przejścia do kalkulatora naukowego
    const scientificBtn = document.getElementById('scientificCalcTab');
    if (scientificBtn) {
        scientificBtn.addEventListener('click', () => {
            window.location.href = '/scientific';
        });
    }
});

//MATRIX
// Funkcja do generowania dynamicznej macierzy (A lub B)
function generateMatrix(id) {
    // Pobieramy liczbę wierszy i kolumn z inputów
    const rows = document.getElementById(`rows${id}`).value;
    const cols = document.getElementById(`cols${id}`).value;

    // Znajdujemy kontener, w którym będziemy wyświetlać macierz
    const container = document.getElementById(`matrix${id}Container`);

    // Czyścimy kontener przed dodaniem nowej macierzy
    container.innerHTML = `<h4>Macierz ${id}</h4>`;

    // Tworzymy tabelę, która będzie reprezentować macierz
    const table = document.createElement("table");
    table.classList.add("matrix"); // Dodajemy klasę do stylizacji tabeli

    // Pętla tworząca wiersze i kolumny w tabeli
    for (let i = 0; i < rows; i++) {
        const tr = document.createElement("tr"); // Tworzymy wiersz

        // Pętla tworząca komórki w każdym wierszu
        for (let j = 0; j < cols; j++) {
            const td = document.createElement("td"); // Tworzymy komórkę
            const input = document.createElement("input"); // Tworzymy input do wprowadzania danych
            input.type = "number"; // Określamy, że input będzie typu liczba
            input.name = `${id}[${i}][${j}]`; // Nadajemy unikalną nazwę dla każdego inputu w zależności od macierzy i pozycji

            td.appendChild(input); // Dodajemy input do komórki
            tr.appendChild(td); // Dodajemy komórkę do wiersza
        }

        table.appendChild(tr); // Dodajemy wiersz do tabeli
    }

    // Dodajemy tabelę z macierzą do kontenera
    container.appendChild(table);
}

// Funkcja obsługująca operacje na macierzach (do uzupełnienia przy backendzie)
function performOperation(type) {
    // Na razie wyświetlamy tylko nazwę operacji w kontenerze wyników
    document.getElementById("resultContainer").innerText = `Wykonano operację: ${type}`;
}
