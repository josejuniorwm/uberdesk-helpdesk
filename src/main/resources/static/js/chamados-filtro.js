document.addEventListener("DOMContentLoaded", function () {
    const inputBusca = document.getElementById("filtroBusca");
    const selectStatus = document.getElementById("filtroStatus");
    const selectPrioridade = document.getElementById("filtroPrioridade");
    const btnLimpar = document.getElementById("btnLimparFiltros");
    const linhas = document.querySelectorAll(".linha-chamado");

    if (!inputBusca || !selectStatus || !selectPrioridade) {
        return;
    }

    function filtrarChamados() {
        const termo = inputBusca.value.trim().toLowerCase();
        const statusFiltro = selectStatus.value;
        const prioridadeFiltro = selectPrioridade.value;

        linhas.forEach(linha => {
            const id = (linha.dataset.id || "").toLowerCase();
            const titulo = (linha.dataset.titulo || "").toLowerCase();
            const status = linha.dataset.status || "";
            const prioridade = linha.dataset.prioridade || "";

            const bateBusca = termo === "" || id.includes(termo) || titulo.includes(termo);

            let bateStatus = false;
            if (statusFiltro === "TODOS") {
                bateStatus = true;
            } else if (statusFiltro === "ATIVOS") {
                bateStatus = (status !== "ENCERRADO");
            } else {
                bateStatus = (status === statusFiltro);
            }

            const batePrioridade = prioridadeFiltro === "TODAS" || prioridade === prioridadeFiltro;

            linha.style.display = (bateBusca && bateStatus && batePrioridade) ? "" : "none";
        });
    }

    inputBusca.addEventListener("input", filtrarChamados);
    selectStatus.addEventListener("change", filtrarChamados);
    selectPrioridade.addEventListener("change", filtrarChamados);

    if (btnLimpar) {
        btnLimpar.addEventListener("click", function () {
            inputBusca.value = "";
            selectStatus.value = "ATIVOS";
            selectPrioridade.value = "TODAS";
            filtrarChamados();
        });
    }

    // Aplica o filtro inicial ao carregar a página (oculta os ENCERRADOS por padrão)
    filtrarChamados();
});