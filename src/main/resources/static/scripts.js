document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('expression');

    // Funkcja obsługująca przyciski na ekranie
    function addDigit(char) {
        input.value += char;
    }

    // Funkcja czyszcząca pole wejściowe
    function clearInput() {
        input.value = '';
    }

    // Obsługuje klawisze fizyczne
    document.addEventListener('keydown', (event) => {
        const allowedKeys = '0123456789+-*/().%'; // Dozwolone znaki

        // Obsługuje tylko dozwolone znaki
        if (allowedKeys.includes(event.key)) {
            event.preventDefault(); // Zapobiega domyślnym akcjom, takim jak przewijanie strony
            // Dodajemy znak, ale sprawdzamy, czy nie dodajemy dwóch kropek
            if (event.key === '.' && !input.value.includes('.')) {
                input.value += event.key; // Dodajemy tylko jedną kropkę
            } else if (event.key !== '.') {
                input.value += event.key;
            }
        }

        // Zamiana przecinka z klawiatury numerycznej na kropkę
        if (event.key === ',') {
            event.preventDefault();
            input.value += '.'; // Zamiana przecinka na kropkę
        }

        // Obsługuje Enter - submit formularza
        if (event.key === 'Enter') {
            event.preventDefault();
            document.querySelector('.calculator-form').submit();
        }

        // Obsługuje Backspace - usuwanie ostatniego znaku
        if (event.key === 'Backspace') {
            event.preventDefault();
            input.value = input.value.slice(0, -1); // Usuwa ostatni znak
        }

        // Obsługuje Escape - czyszczenie pola
        if (event.key === 'Escape') {
            event.preventDefault();
            clearInput(); // Czyszczenie pola
        }
    });

    // Podłączenie przycisku kalkulatora naukowego
    document.getElementById('scientificCalcTab').addEventListener('click', () => {
        window.location.href = '/scientific';
    });
});
