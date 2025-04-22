package com.example.demo.service;

import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_AAC;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AudioService {

  private static final String FFMPEG_PATH = "C:/ffmpeg-master-latest-win64-gpl-shared/bin/ffmpeg.exe";
  private static final Pattern DURATION_PATTERN = Pattern.compile("Duration: (\\d{2}):(\\d{2}):(\\d{2})\\.(\\d{2})");
  private static final String FFPROBE_PATH = "C:/ffmpeg-master-latest-win64-gpl-shared/bin/ffprobe.exe";

  /**
   * Obtém a duração de um arquivo de mídia em segundos.
   *
   * @param filePath Caminho para o arquivo de mídia.
   * @return Duração em segundos, ou -1.0 se não puder ser determinada.
   */
  public static Double getMediaDurationSeconds(String filePath) {
    List<String> command = new ArrayList<>();
    command.add(FFPROBE_PATH);
    command.add("-v");
    command.add("error");
    command.add("-show_entries");
    command.add("format=duration");
    command.add("-of");
    command.add("default=noprint_wrappers=1:nokey=1");
    command.add(filePath);

    try {

      log.info("Busca duração do audio");

      ProcessBuilder processBuilder = new ProcessBuilder(command);
      Process process = processBuilder.start();

      StringBuilder output = new StringBuilder();

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          output.append(line);
        }
      }

      try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
          log.error("FFprobe Error: " + errorLine);
        }
      }

      boolean finished = process.waitFor(30, TimeUnit.SECONDS);
      int exitCode = process.exitValue();

      if (finished && exitCode == 0 && output.length() > 0) {
        return Double.parseDouble(output.toString().trim());
      } else {
        System.err.println(
            "Falha ao obter duração via ffprobe. Exit code: " + exitCode + (finished ? "" : " (Timeout)"));
        return getDurationFromFullOutput(filePath);
      }

    } catch (Exception e) {
      System.err.println("Erro ao executar ffprobe: " + e.getMessage());
      e.printStackTrace();
      return getDurationFromFullOutput(filePath);
    }
  }

  /**
   * Método alternativo para obter a duração do tempo dp audio. É acessado caso o método acima falhe.
   *
   * @param filePath Caminho para o arquivo de mídia.
   * @return Duração em segundos, ou -1.0 se não puder ser determinada.
   */
  public static Double getDurationFromFullOutput(String filePath) {
    List<String> command = new ArrayList<>();
    command.add(FFPROBE_PATH);
    command.add("-i");
    command.add(filePath);

    try {
      ProcessBuilder processBuilder = new ProcessBuilder(command);
      processBuilder.redirectErrorStream(true);
      Process process = processBuilder.start();

      StringBuilder fullOutput = new StringBuilder();
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          fullOutput.append(line).append("\n");
        }
      }

      boolean finished = process.waitFor(30, TimeUnit.SECONDS);

      if (finished) {
        Matcher matcher = DURATION_PATTERN.matcher(fullOutput.toString());

        if (matcher.find()) {
          double hours = Double.parseDouble(matcher.group(1));
          double minutes = Double.parseDouble(matcher.group(2));
          double seconds = Double.parseDouble(matcher.group(3));
          double centiseconds = Double.parseDouble(matcher.group(4));
          return (hours * 3600) + (minutes * 60) + seconds + (centiseconds / 100.0);
        } else {
          log.error("Não foi possível encontrar o padrão de duração na saída completa do ffprobe.");
        }
      } else {
        log.error("Timeout ao obter saída completa do ffprobe.");
      }

    } catch (Exception e) {
      log.error("Erro ao executar ffprobe (alternativa): " + e.getMessage());
    }
    return -1.0;
  }


  /**
   * Divide um arquivo de áudio em segmentos de duração máxima especificada.
   *
   * @param inputAudioPath            Caminho para o áudio original.
   * @param outputDirectory           Diretório onde os segmentos serão salvos.
   * @param outputPrefix              Prefixo para os nomes dos arquivos de saída (ex: "segmento_").
   * @param maxSegmentDurationSeconds Duração máxima de cada segmento em segundos.
   * @param totalDuration             Druação total do audio em segundos.
   * @return Lista com os caminhos completos para os arquivos de áudio divididos, ou lista vazia em caso de falha.
   */
  public static List<String> splitAudio(String inputAudioPath, String outputDirectory, String outputPrefix,
      Double maxSegmentDurationSeconds, Double totalDuration) {

    log.info("Corte do audio");

    List<String> outputFiles = new ArrayList<>();

    File outDir = new File(outputDirectory);

    if (!outDir.exists()) {
      if (!outDir.mkdirs()) {
        log.error("Falha ao criar diretório de saída: " + outputDirectory);
        return outputFiles;
      }
    }

    String extension = "";
    int i = inputAudioPath.lastIndexOf('.');
    if (i > 0) {
      extension = inputAudioPath.substring(i);
    } else {
      log.error("Não foi possível determinar a extensão do arquivo de entrada. Usando .aac por padrão.");
      extension = ".aac";
    }

    int segmentNumber = 1;
    double startTime = 0.0;

    while (startTime < totalDuration) {
      String outputFilePath = Paths.get(outputDirectory, outputPrefix + segmentNumber + extension).toString();
      List<String> command = new ArrayList<>();
      command.add(FFMPEG_PATH);
      command.add("-i");
      command.add(inputAudioPath);
      command.add("-ss");
      command.add(String.format("%.3f", startTime).replace(",", "."));
      command.add("-t");
      command.add(String.format("%.3f", maxSegmentDurationSeconds).replace(",", "."));
      command.add("-vn");
      command.add("-acodec");
      command.add("copy");
      command.add("-y");
      command.add(outputFilePath);

      if (executeCommand(command)) {
        outputFiles.add(outputFilePath);
      } else {
        log.error("Falha ao criar segmento: " + outputFilePath);
        return new ArrayList<>();
      }

      startTime += maxSegmentDurationSeconds;
      segmentNumber++;
    }

    return outputFiles;
  }

  /**
   * Helper para executar comandos (igual ao da classe AudioDurationChecker)
   *
   * @param command Comandos para serem executados.
   * @return Retorna true para sucesso e false para falhas.
   */
  private static boolean executeCommand(List<String> command) {
    System.out.println("Executing command: " + String.join(" ", command));
    try {
      ProcessBuilder processBuilder = new ProcessBuilder(command);
      processBuilder.redirectErrorStream(true);
      Process process = processBuilder.start();

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          System.out.println("FFmpeg Output: " + line);
        }
      }

      boolean finished = process.waitFor(180, TimeUnit.SECONDS);
      int exitCode = process.exitValue();

      if (finished && exitCode == 0) {
        System.out.println("FFmpeg command executed successfully.");
        return true;
      } else {
        System.err.println("FFmpeg command failed. Exit code: " + exitCode + (finished ? "" : " (Timeout)"));
        return false;
      }

    } catch (Exception e) {
      System.err.println("Error executing FFmpeg command: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }


  /**
   * Lê arquivo do diretório local.
   *
   * @param filePath Caminho para o arquivo a ser baixado.
   * @return Retorna conteúdo do arquivo em bytes.
   */
  public static byte[] readFileAudio(String filePath) throws Exception {
    File file = new File(filePath);
    long fileSize = file.length();

    byte[] bytes = new byte[(int) fileSize];

    try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
      fis.read(bytes);
    }
    return bytes;
  }

  /**
   * Extrai o audio de um arquivo de video.
   *
   * @param videoInputPath Caminho para o video.
   * @param audioOutputPath Caminho para a saída de audio.
   */
  public static void extractAudio(String videoInputPath, String audioOutputPath) throws FrameGrabber.Exception, FrameRecorder.Exception {

    log.info("Extração do audio iniciada");

    try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoInputPath)) {
      grabber.start();

      try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(audioOutputPath, grabber.getAudioChannels())) {
        recorder.setAudioBitrate(grabber.getAudioBitrate());

        recorder.setFormat("adts");
        recorder.setAudioCodec(AV_CODEC_ID_AAC);
        recorder.setSampleRate(grabber.getSampleRate());

        recorder.start();

        Frame capturedFrame;

        while ((capturedFrame = grabber.grab()) != null) {

          if (capturedFrame.samples != null) {
            recorder.record(capturedFrame);
          }
        }

      }

      System.out.println("Extração de áudio concluída para: " + audioOutputPath);
    } catch (Exception e) {

      System.err.println("Erro durante a extração de áudio: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }

}
