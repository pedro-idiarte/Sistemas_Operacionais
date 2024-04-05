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
     
      
      Scanner teclado = new Scanner (System.in);
      
      
      popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
      
      imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
      
      //Escolher algoritmo
      int alg;
      
      while(true) {
        System.out.print("\nEscolha o argoritmo?:\n"+"1=FCFS\n" + "2=SJF Preemptivo\n" + "3=SJF Não Preemptivo\n" +"4=Prioridade Preemptivo\n" + "5=Prioridade Não Preemptivo\n" + "6=Round_Robin\n" + "7=Imprime lista de processos\n" + "8=Popular processos novamente\n" + "9=Sair: ");
        alg =  teclado.nextInt();
        
        
        if (alg == 1) { //FCFS
            FCFS(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
        }
        else if (alg == 2) { //SJF PREEMPTIVO
            SJF(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada); // SJF preemptivo
        }
        else if (alg == 3) { //SJF NÃO PREEMPTIVO
            SJF(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada); // SJF não preemptivo
            
        }
        else if (alg == 4) { //PRIORIDADE PREEMPTIVO
            PRIORIDADE(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
        }
        else if (alg == 5) { //PRIORIDADE NÃO PREEMPTIVO
        	PRIORIDADE(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            
        }
        else if (alg == 6) { //Round_Robin
        	Round_Robin(tempo_execucao, tempo_espera, tempo_restante);
            
        }
        else if (alg == 7) { //IMPRIME CONTEÚDO INICIAL DOS PROCESSOS
        	imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
        }

        else if (alg == 8) { //REATRIBUI VALORES INICIAIS
            popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
        }
        else if (alg == 9) {
            break;
            
        }
    }
              
    }

    public static void popular_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada,  int [] prioridade ){
        Random random = new Random();
        Scanner teclado = new Scanner (System.in);
        int aleatorio;

        System.out.print("Será aleatório?:  ");
        aleatorio =  teclado.nextInt();

        for (int i = 0; i < n_processos; i++) {
            //Popular Processos Aleatorio
            if (aleatorio == 1){
                tempo_execucao[i] = random.nextInt(10)+1;
                tempo_chegada[i] = random.nextInt(10)+1;
                prioridade[i] = random.nextInt(15)+1;
            }
            //Popular Processos Manual
            else {
                System.out.print("Digite o tempo de execução do processo["+i+"]:  ");
                tempo_execucao[i] = teclado.nextInt();
                System.out.print("Digite o tempo de chegada do processo["+i+"]:  ");
                tempo_chegada[i] = teclado.nextInt();
                System.out.print("Digite a prioridade do processo["+i+"]:  ");
                prioridade[i] = teclado.nextInt();
            }
            tempo_restante[i] = tempo_execucao[i];
    
          }
    }

    public static void imprime_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada,  int []prioridade){
        //Imprime lista de processos
      for (int i = 0; i < n_processos; i++) {
        System.out.println("\nProcesso["+i+"]: tempo_execucao="+ tempo_execucao[i] + " tempo_restante="+tempo_restante[i] + " tempo_chegada=" + tempo_chegada[i] + " prioridade =" +prioridade[i]);
    }
    }

    public static void imprime_stats (int[] espera) {
        int[] tempo_espera = espera.clone();
        //Implementar o calculo e impressão de estatisticas
        
        double tempo_espera_total = 0;
        
        for(int i=0; i<n_processos; i++){ 
            System.out.println("Processo["+i+"]: tempo_espera="+tempo_espera[i]);
            tempo_espera_total = tempo_espera_total + tempo_espera[i];
        }

        System.out.println("Tempo médio de espera: "+(tempo_espera_total/n_processos));
        
    }
    
    public static void FCFS(int[] execucao, int[] espera, int[] restante, int[] chegada){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        //int[] tempo_chegada = chegada.clone();

        int processo_em_execucao = 0; //processo inicial no FIFO é o zero

        //implementar código do FCFS
        for (int i=1; i<MAXIMO_TEMPO_EXECUCAO; i++) {
            System.out.println("\ntempo["+i+"]: processo["+processo_em_execucao+"] restante="+tempo_restante[processo_em_execucao]);
            
            if (tempo_execucao[processo_em_execucao] == tempo_restante[processo_em_execucao])
                tempo_espera[processo_em_execucao] = i-1;

            if (tempo_restante[processo_em_execucao] == 1) {
                if (processo_em_execucao == (n_processos-1))
                    break;
                else
                    processo_em_execucao++;
            }
            else
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
    
        int tempo_atual = 1; // Começando a contagem de tempo em 1
        int processos_completos = 0;
    
        while (processos_completos < n_processos) {
            int menor_tempo_execucao = Integer.MAX_VALUE;
            int processo_menor_tempo = -1;
    
            for (int i = 0; i < n_processos; i++) {
                if (tempo_chegada[i] <= tempo_atual && tempo_restante[i] < menor_tempo_execucao && tempo_restante[i] > 0) {
                    menor_tempo_execucao = tempo_restante[i];
                    processo_menor_tempo = i;
                }
            }
    
            if (processo_menor_tempo == -1) {
                System.out.println("\ntempo[" + tempo_atual + "]: nenhum processo está pronto");
                tempo_atual++;
                continue;
            }
    
            // Execução do processo
            tempo_restante[processo_menor_tempo]--;
            System.out.println("\ntempo[" + tempo_atual + "]: processo[" + processo_menor_tempo + "] restante=" + tempo_restante[processo_menor_tempo]);
    
            if (tempo_restante[processo_menor_tempo] == 0) {
                processos_completos++;
    
                // Calcula o tempo de espera para o processo concluído
                int tempo_espera_processo = tempo_atual - tempo_execucao[processo_menor_tempo] + 1 - tempo_chegada[processo_menor_tempo];
                tempo_espera[processo_menor_tempo] = tempo_espera_processo >= 0 ? tempo_espera_processo : 0;
            }
    
            tempo_atual++;
        }
    
        imprime_stats(tempo_espera);
    }
    
    
    
    public static void PRIORIDADE(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada, int[] prioridade){
    	int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();
        int[] prioridade_temp = prioridade.clone();

        //implementar código do Prioridade preemptivo e não preemptivo
        //...
        //

          imprime_stats(tempo_espera);
      
    }
    
    public static void Round_Robin(int[] execucao, int[] espera, int[] restante){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();

        
        //implementar código do Round-Robin
        //...
        //
        
        imprime_stats(tempo_espera);
    }
}