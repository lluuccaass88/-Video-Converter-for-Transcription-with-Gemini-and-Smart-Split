## Video Converter for Transcription with Gemini and Smart Split (EchoBot 🤖)

✅ Este é um projeto pessoal desenvolvido em Java com o objetivo de automatizar a transcrição de vídeos, utilizando a API do Gemini (anteriormente Google AI) e dividindo vídeos longos em partes menores para processamento eficiente.

### 1️⃣ Funcionalidades

* **Entrada:** Recebe um arquivo de vídeo como entrada.
* **Extração de Áudio:** Extrai o áudio do arquivo de vídeo utilizando FFmpeg.
* **Divisão Inteligente:** Divide o áudio extraído em partes de até 40 minutos para otimizar o processamento pela API do Gemini. Isso é especialmente útil para vídeos longos, evitando requisições extensas e potenciais timeouts.
* **Transcrição com Gemini:** Envia cada parte do áudio para a API do Gemini para transcrição.
* **Saída em Texto:** Salva a transcrição de todas as partes em um único arquivo de texto (`.txt`).

### 2️⃣ Tecnologias Utilizadas

* **Java:** Linguagem de programação principal.
* **Google Gemini API:** Para realizar a transcrição do áudio.
* **FFmpeg:** Ferramenta de linha de comando para manipulação de arquivos multimídia (extração e divisão de áudio).
* **FFprobe:** Utilitário do FFmpeg para obter informações sobre arquivos multimídia (utilizado para verificar a duração do áudio).
* **JavaCV:** Biblioteca Java wrapper para OpenCV, FFmpeg e outras bibliotecas de visão computacional e multimídia (utilizada para facilitar a interação com o FFmpeg).
* **RestTemplate (Spring Framework):** Para realizar requisições HTTP para a API do Gemini.

### 3️⃣ Pré-requisitos

* **Java Development Kit (JDK):** Certifique-se de ter o JDK instalado em sua máquina.
* **FFmpeg:** É necessário ter o FFmpeg instalado e configurado no seu sistema, de forma que os comandos `ffmpeg` e `ffprobe` possam ser executados a partir da linha de comando. Você pode encontrar instruções de instalação para o seu sistema operacional em [https://ffmpeg.org/download.html](https://ffmpeg.org/download.html).
* **Chave da API do Gemini:** Você precisará obter uma chave da API do Gemini para autenticar suas requisições. Consulte a documentação oficial do Google AI para obter mais informações sobre como criar e gerenciar chaves de API.

### 4️⃣ Configuração

1.  **Clone o repositório:**
    ```bash
    git clone https://[link-do-seu-repositorio]
    cd [nome-do-seu-repositorio]
    ```

2.  **Configure a chave da API do Gemini:**
    * Localize o arquivo onde a chave da API é utilizada no código Java (geralmente uma String constante ou uma variável).
    * Substitua o valor padrão pela sua chave da API do Gemini.

3.  **Opcional: Configurar diretórios (se aplicável):**
    * Se o projeto espera que os vídeos sejam colocados em um diretório específico, certifique-se de criar esse diretório e ajustar o caminho no código, se necessário.

### 5️⃣ Como Executar

1.  **Compile o código Java:**
    ```bash
    javac src/*.java
    ```
    (Assumindo que seus arquivos `.java` estão em um diretório `src`)

2.  **Execute a aplicação:**
    ```bash
    java Main [caminho/para/o/seu/video.mp4] [caminho/para/a/pasta/de/saida/]
    ```
    * Substitua `[caminho/para/o/seu/video.mp4]` pelo caminho completo para o arquivo de vídeo que você deseja transcrever.
    * Substitua `[caminho/para/a/pasta/de/saida/]` pelo caminho completo para a pasta onde você deseja que o arquivo de texto com a transcrição seja salvo.

    **Observação:** A forma exata de executar pode variar dependendo da estrutura do seu projeto. Consulte o arquivo principal (`Main.java` ou similar) para entender os argumentos esperados.

### 🚀 Melhorias Futuras

* **Personalização do Prompt:** Permitir que o usuário configure o prompt enviado para a API do Gemini através da linha de comando ou de um arquivo de configuração.
* **Tratamento de Erros Aprimorado:** Implementar um tratamento de exceptions mais robusto para lidar com diferentes cenários de erro (falha na API, problemas com arquivos, etc.).
* **Melhoria na Saída do Arquivo:** Formatar a saída do arquivo de texto de forma mais clara, especialmente quando o vídeo é dividido em várias partes. Incluir indicadores de qual parte do vídeo corresponde cada seção da transcrição.
* **Interface Gráfica (GUI):** Desenvolver uma interface gráfica para facilitar o uso da aplicação para usuários não técnicos.
* **Suporte a Múltiplos Formatos de Vídeo:** Expandir o suporte para outros formatos de vídeo além do MP4.
* **Opções de Idioma:** Permitir que o usuário especifique o idioma do vídeo para otimizar a transcrição.

### 🙏 Contribuição

Contribuições são bem-vindas! Se você tiver alguma sugestão de melhoria, correção de bugs ou novas funcionalidades, sinta-se à vontade para abrir uma issue ou enviar um pull request.
