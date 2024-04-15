package base;

import java.util.Scanner;

import java.util.Random;

public class base {

    static int MAXIMO_TEMPO_EXECUCAO = 65535;

    static int n_processos = 3;

    public static void main(String[] args) {

        int[] tempo_execucao = new int[n_processos];
        int[] tempo_chegada = new int[n_processos];
        int[] prioridade = new int[n_processos];
        int[] tempo_espera = new int[n_processos];
        int[] tempo_restante = new int[n_processos];

        Scanner teclado = new Scanner(System.in);

        popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);

        imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);

        // ID do algoritmo
        int alg;

        // Escolher algoritmo
        while (true) {

            System.out.print(
                    "\nEscolha o argoritmo?: \n1=FCFS \n2=SJF Preemptivo \n3=SJF Não Preemptivo  \n4=Prioridade Preemptivo \n5=Prioridade Não Preemptivo  \n6=Round_Robin  \n7=Imprime lista de processos \n8=Popular processos novamente \n9=Sair: ");

            alg = teclado.nextInt();

            switch (alg) {
                case 1 -> FCFS(tempo_execucao, tempo_espera, tempo_restante, null);
                case 2 -> SJF(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
                case 3 -> SJF(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
                case 4 -> PRIORIDADE(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                case 5 -> PRIORIDADE(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                case 6 -> Round_Robin(tempo_execucao, tempo_espera, tempo_restante);
                case 7 -> imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                case 8 -> {
                    popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                    imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                }
                case 9 -> {
                    break;
                }
            }
        }

    }

    public static void list(int tempo, int processo, int[] restante) {
        System.out.println("tempo[" + tempo + "]: processo[" + processo + "] restante=" + restante[processo]);
    }

    public static void popular_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante,
            int[] tempo_chegada, int[] prioridade) {
        Random random = new Random();
        Scanner teclado = new Scanner(System.in);
        int aleatorio;

        System.out.print("Será aleatório? 1=Sim - 2=Não:  ");
        aleatorio = teclado.nextInt();

        for (int i = 0; i < n_processos; i++) {
            // Popular Processos Aleatorio
            if (aleatorio == 1) {
                tempo_execucao[i] = random.nextInt(10) + 1;
                tempo_chegada[i] = random.nextInt(10) + 1;
                prioridade[i] = random.nextInt(15) + 1;
            }
            // Popular Processos Manual
            else {
                System.out.print("Digite o tempo de execução do processo[" + i + "]:  ");
                tempo_execucao[i] = teclado.nextInt();
                System.out.print("Digite o tempo de chegada do processo[" + i + "]:  ");
                tempo_chegada[i] = teclado.nextInt();
                System.out.print("Digite a prioridade do processo[" + i + "]:  ");
                prioridade[i] = teclado.nextInt();
            }
            tempo_restante[i] = tempo_execucao[i];

        }
    }

    public static void imprime_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante,
            int[] tempo_chegada, int[] prioridade) {
        // Imprime lista de processos
        for (int i = 0; i < n_processos; i++) {
            System.out.println("Processo[" + i + "]: tempo_execucao=" + tempo_execucao[i] + " tempo_restante="
                    + tempo_restante[i] + " tempo_chegada=" + tempo_chegada[i] + " prioridade =" + prioridade[i]);
        }
    }

    public static void imprime_stats(int[] espera) {
        int[] tempo_espera = espera.clone();
        // Implementar o calculo e impressão de estatisticas

        double tempo_espera_total = 0;

        for (int i = 0; i < n_processos; i++) {
            System.out.println("Processo[" + i + "]: tempo_espera=" + tempo_espera[i]);
            tempo_espera_total = tempo_espera_total + tempo_espera[i];
        }

        System.out.println("\nTempo médio de espera: " + (tempo_espera_total / n_processos));

    }

    public static void FCFS(int[] execucao, int[] espera, int[] restante, int[] chegada) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        // int[] tempo_chegada = chegada.clone();

        int processo_em_execucao = 0; // processo inicial no FIFO é o zero

        // implementar código do FCFS
        for (int i = 1; i < MAXIMO_TEMPO_EXECUCAO; i++) {
            System.out.println("tempo[" + i + "]: processo[" + processo_em_execucao + "] restante="
                    + tempo_restante[processo_em_execucao]);

            if (tempo_execucao[processo_em_execucao] == tempo_restante[processo_em_execucao])
                tempo_espera[processo_em_execucao] = i - 1;

            if (tempo_restante[processo_em_execucao] == 1) {
                if (processo_em_execucao == (n_processos - 1))
                    break;
                else
                    processo_em_execucao++;
            } else
                tempo_restante[processo_em_execucao]--;

        }
        //

        imprime_stats(tempo_espera);
    }

    public static void SJF(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();

        // Inicialização de variáveis / menor tempo
        int menorTempo = MAXIMO_TEMPO_EXECUCAO; // Inicializa o menor tempo com um valor muito grande
        int processoEmExecucao = -1; // Inicializa o índice do processo em execução como -1 (nenhum processo em
                                     // execução)

        int concluidos = 0; // Contador de processos concluídos
        int tempo = 0; // Tempo de execução inicializado em 0

        // Loop principal que simula a execução dos processos
        while (true) {
            tempo++; // Incrementa o tempo de execução

            // Verifica se o algoritmo é preemptivo ou se nenhum processo está em execução
            if (preemptivo || processoEmExecucao == -1) {
                // Percorre todos os processos para encontrar o próximo a ser executado
                for (int i = 0; i < n_processos; i++) {
                    // Verifica se o processo está pronto para execução e se tem tempo restante
                    if ((tempo_restante[i] != 0) && (tempo_chegada[i] <= tempo)) {
                        // Verifica se o tempo restante deste processo é menor que o menor tempo atual
                        if (tempo_restante[i] < menorTempo) {
                            menorTempo = tempo_restante[i]; // Atualiza o menor tempo
                            processoEmExecucao = i; // Atualiza o índice do processo a ser executado
                        }
                    }
                }
            }
            // Verifica se não há nenhum processo pronto para execução
            if (processoEmExecucao == -1)
                System.out.println("tempo[" + tempo + "]: nenhum processo está pronto");
            else {

                // Verifica se é a primeira vez que o processo é executado para calcular o tempo
                // de espera
                if (tempo_execucao[processoEmExecucao] == tempo_restante[processoEmExecucao])
                    tempo_espera[processoEmExecucao] = tempo - tempo_chegada[processoEmExecucao];

                tempo_restante[processoEmExecucao]--; // Decrementa o tempo restante do processo em execução
                list(tempo, processoEmExecucao, tempo_restante); // Lista o tempo atual e o processo em execução

                // Verifica se o processo em execução foi concluído
                if (tempo_restante[processoEmExecucao] == 0) {
                    processoEmExecucao = -1; // Nenhum processo em execução
                    menorTempo = MAXIMO_TEMPO_EXECUCAO; // Reseta o menor tempo
                    concluidos++; // Incrementa o contador de processos concluídos

                    // Verifica se todos os processos foram concluídos
                    if (concluidos == n_processos)
                        break; // Sai do loop
                }
            }
        }
        imprime_stats(tempo_espera);
    }

    public static void PRIORIDADE(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada,int[] prioridade) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();
        int[] _prioridade = prioridade.clone();

        // Variável para armazenar a maior prioridade encontrada
        int maiorPrioridade = 0;

        // Ao invés de usar boolean, setar processoEmExecução para -1 enquanto nenhum
        // processo chegar na fila de pronto
        int processoEmExecucao = -1;

        int concluidos = 0; // Contador de processos concluídos
        int tempo = 0; // Tempo atual de execução

        // Loop principal que simula a execução dos processos
        while (true) {
            tempo++; // Incrementa o tempo em cada iteração

            // Verifica se o algoritmo é preemptivo ou se nenhum processo está em execução
            if (preemptivo || processoEmExecucao == -1) {
                // Loop para encontrar o próximo processo a ser executado
                for (int proc = 0; proc < n_processos; proc++) {
                    // Verifica se o processo está pronto para execução e se sua prioridade é maior
                    if ((tempo_restante[proc] != 0) && (tempo_chegada[proc] <= tempo)) {
                        if (_prioridade[proc] > maiorPrioridade) {
                            // Atualiza o processo em execução com o de maior prioridade
                            maiorPrioridade = _prioridade[proc];
                            processoEmExecucao = proc;
                        }
                    }
                }
            }
            // Verifica se nenhum processo está pronto para execução
            if (processoEmExecucao == -1)
                System.out.println("tempo[" + tempo + "]: nenhum processo está pronto");
            else {
                // Calcula o tempo de espera se o processo acabou de chegar na fila de pronto
                if (tempo_restante[processoEmExecucao] == tempo_execucao[processoEmExecucao])
                    tempo_espera[processoEmExecucao] = tempo - tempo_chegada[processoEmExecucao];

                // Executa uma unidade de tempo do processo em execução
                tempo_restante[processoEmExecucao]--;
                list(tempo, processoEmExecucao, tempo_restante);

                // Verifica se o processo foi concluído
                if (tempo_restante[processoEmExecucao] == 0) {
                    // Reseta o processo em execução e a maior prioridade
                    processoEmExecucao = -1;
                    maiorPrioridade = 0;
                    concluidos++;

                    // Verifica se todos os processos foram concluídos
                    if (concluidos == n_processos)
                        break;
                }
            }
        }
        // Após o término da simulação, imprime as estatísticas de espera dos processos
        imprime_stats(tempo_espera);
    }

    public static void Round_Robin(int[] execucao, int[] espera, int[] restante) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();

        // implementar código do Round-Robin
        // ...
        //

        imprime_stats(tempo_espera);
    }
}