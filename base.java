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
                case 9 -> { System.out.println("\nPrograma Encerrado");
                    System. exit(0);
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
            System.out.println("\nProcesso[" + i + "]: tempo_execucao=" + tempo_execucao[i] + " tempo_restante="
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
        //int[] tempo_chegada = chegada.clone();

        int processo_em_execucao = 0; // Inicializa o índice do processo em execução como 0 (primeiro processo na fila)

        // Implementação do algoritmo FCFS (First-Come, First-Served)
        for (int i = 1; i < MAXIMO_TEMPO_EXECUCAO; i++) {
            // Lista o tempo atual e o processo em execução
            System.out.println("tempo[" + i + "]: processo[" + processo_em_execucao + "] restante="
                    + tempo_restante[processo_em_execucao]);

            // Verifica se é a primeira vez que o processo é executado para calcular o tempo de espera
            if (tempo_execucao[processo_em_execucao] == tempo_restante[processo_em_execucao])
                tempo_espera[processo_em_execucao] = i - 1;

            // Verifica se o tempo restante do processo é igual a 1 (último ciclo de execução)
            if (tempo_restante[processo_em_execucao] == 1) {
                // Verifica se é o último processo na fila
                if (processo_em_execucao == (n_processos - 1))
                    break; // Sai do loop, pois todos os processos foram concluídos
                else
                    processo_em_execucao++; // Avança para o próximo processo na fila
            } else
                tempo_restante[processo_em_execucao]--; // Decrementa o tempo restante do processo
 
        }
        imprime_stats(tempo_espera);
    }

    public static void SJF(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();

        int menorTempo = MAXIMO_TEMPO_EXECUCAO;
        int processoExecucao = -1; // Processos em execução
        int processosConcluidos = 0;
        int tempo = 0;

        System.out.println("--------------------------------");

        while(true){
            tempo++;
            //PREEMPTIVO
            if(preemptivo || processoExecucao == -1){ //se for preemptivo ou os processos estiverem iguais ao início
                for(int i=0; i <n_processos; i++){ // loop até acabar o número de processos

                    if((tempo_restante[i] != 0) && (tempo_chegada[i] <= tempo)){ //Se o tempo restante atual for diferente de 0 e o tempo de chegada for menor ou igual ao tempo
                        if(tempo_restante[i] < menorTempo){ // se o tempo restante atual for menor que o menor tempo
                            menorTempo = tempo_restante[i]; //então o menor tempo é o tempo restante atual
                            processoExecucao = i; // e o processo que está em execução é o atual
                        }
                    } 
                }
            }
            if(processoExecucao == -1){ //se processos em execução estiver igual, nenhum processo ficou pronto
                System.out.println("tempo["+tempo+"]: nenhum processo está pronto");
            
            }else{ // NÃO PREEMPTIVO
                
                if(tempo_execucao[processoExecucao] == tempo_restante[processoExecucao])
                tempo_espera[processoExecucao] = tempo - tempo_chegada[processoExecucao];

                tempo_restante[processoExecucao]--;
                System.out.println("tempo[" + tempo + "]: processo[" + processoExecucao + "] restante = " + tempo_restante[processoExecucao]);

                if(tempo_restante[processoExecucao] == 0){
                    processoExecucao = -1;
                    menorTempo = MAXIMO_TEMPO_EXECUCAO;
                    processosConcluidos++;

                    if(processosConcluidos == n_processos)
                    break;
                }
            }
        }
        System.out.println("--------------------------------");
        imprime_stats(tempo_espera);
    }

    public static void PRIORIDADE(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada, int[] prioridade) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();
        int[] prioridade_ = prioridade.clone();

        int maiorPrioridade = 0; //esse programa utiliza da maior prioridade para ser o primeiro processo a ser feito
        int processoExecucao = -1;
        int processosConcluidos = 0;
        int tempo = 0;

        while(true) { 
            tempo++;

            //PREEMPTIVO
            // Escalonamento preemptivo: seleciona o processo com maior prioridade que chegou até o momento
            if (preemptivo || processoExecucao == -1) {
                for (int i=0; i<n_processos; i++) {
                    if ((tempo_restante[i] != 0) && (tempo_chegada[i] <= tempo)) {
                        if (prioridade_[i] > maiorPrioridade) {
                            maiorPrioridade = prioridade_[i];
                            processoExecucao = i;
                        }
                    }
                }
            }
            // Verifica se nenhum processo está pronto para execução
            if (processoExecucao == -1)
            System.out.println("tempo["+tempo+"]: nenhum processo está pronto");

            else {
                // Atualiza o tempo de espera do processo que acabou de chegar à CPU
                if (tempo_restante[processoExecucao] == tempo_execucao[processoExecucao])
                    tempo_espera[processoExecucao] = tempo - tempo_chegada[processoExecucao];

                // Executa o processo por 1 unidade de tempo
                tempo_restante[processoExecucao]--;
                System.out.println("tempo[" + tempo + "]: processo[" + processoExecucao + "] restante = " + tempo_restante[processoExecucao]);

                // Verifica se o processo foi concluído
                if (tempo_restante[processoExecucao] == 0) {
                    processoExecucao = -1;
                    maiorPrioridade = 0;
                    processosConcluidos++;

                    if (processosConcluidos == n_processos)
                        break;
                }
            }
        }
        imprime_stats(tempo_espera);
    }

    public static void Round_Robin(int[] execucao, int[] espera, int[] restante) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();

        Scanner tc = new Scanner(System.in);

        // Solicita ao usuário o tamanho da fatia de tempo (time slice)
        System.out.println(" ");
        System.out.println("Escolha o tamanho da Time Slice: ");
        int timeslice = tc.nextInt();
        System.out.println(" ");

        int timesliceCount = 0; // Contador para controlar a fatia de tempo atual
        int concluidos = 0; // Contador para acompanhar quantos processos foram concluídos

        int processoEmExecucao = 0; // Índice do processo em execução
        int tempo = 0; // Tempo atual

        // Loop principal que simula a execução do algoritmo Round Robin
        while(true){
            tempo++; // Incrementa o tempo global
            timesliceCount++; // Incrementa o contador da fatia de tempo atual
            list(tempo, processoEmExecucao, tempo_restante); // Lista o tempo atual e o processo em execução

            // Verifica se é o primeiro ciclo de execução do processo atual para calcular o tempo de espera
            if(tempo_restante[processoEmExecucao] == tempo_execucao[processoEmExecucao]) {
                tempo_espera[processoEmExecucao] = tempo;
            }

            // Verifica se o processo atual foi concluído
            if(tempo_restante[processoEmExecucao] <= 0){
                concluidos++;
            }

            // Verifica se o processo atual ainda tem tempo restante para execução
            if(tempo_restante[processoEmExecucao] > 0){
                tempo_restante[processoEmExecucao]--; // Decrementa o tempo restante do processo
            }else{
                processoEmExecucao++; // Avança para o próximo processo na fila
                timesliceCount = 0; // Reinicia o contador da fatia de tempo atual
            }

            // Verifica se a fatia de tempo atual foi completada
            if(timesliceCount >= timeslice){
                processoEmExecucao++; // Avança para o próximo processo na fila
                timesliceCount = 0; // Reinicia o contador da fatia de tempo atual
            }

            // Verifica se todos os processos foram percorridos e retorna ao início da fila
            if(processoEmExecucao >= n_processos){
                processoEmExecucao = 0; // Retorna ao início da fila
            }

            // Verifica se todos os processos foram concluídos e encerra o loop
            if(concluidos == n_processos){
                break;
            }
        }
        
        imprime_stats(tempo_espera);
    }
   
}