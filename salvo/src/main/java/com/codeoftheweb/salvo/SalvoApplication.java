package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, SalvoRepository salvoRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, ScoreRepository scoreRepository){
		return args -> {
			Player player1 = playerRepository.save(new Player("Jack Bauer", "j.bauer@ctu.gov", "24"));
			Player player2 = playerRepository.save(new Player("Chloe O'Brian", "c.obrian@ctu.gov", "42"));
			Player player3 = playerRepository.save(new Player("Kim Bauer", "kim_bauer@gmail.com", "kb"));
			Player player4 = playerRepository.save(new Player("Tony Almeida", "t.almeida@ctu.gov", "mole"));

			Date date = new Date();
			int offset = 3600;

			Game game1 = gameRepository.save(new Game(date));
			Game game2 = gameRepository.save(new Game(Date.from(date.toInstant().plusSeconds(offset))));
			Game game3 = gameRepository.save(new Game(Date.from(date.toInstant().plusSeconds(offset * 2))));
			Game game4 = gameRepository.save(new Game(Date.from(date.toInstant().plusSeconds(offset * 3))));
			Game game5 = gameRepository.save(new Game(Date.from(date.toInstant().plusSeconds(offset * 4))));
			Game game6 = gameRepository.save(new Game(Date.from(date.toInstant().plusSeconds(offset * 5))));
			Game game7 = gameRepository.save(new Game(Date.from(date.toInstant().plusSeconds(offset * 6))));
			Game game8 = gameRepository.save(new Game(Date.from(date.toInstant().plusSeconds(offset * 7))));

			GamePlayer gamePlayer11 = gamePlayerRepository.save(new GamePlayer(game1.getCreationDate(), player1, game1));
			GamePlayer gamePlayer12 = gamePlayerRepository.save(new GamePlayer(game1.getCreationDate(), player2, game1));
			GamePlayer gamePlayer21 = gamePlayerRepository.save(new GamePlayer(game2.getCreationDate(), player1, game2));
			GamePlayer gamePlayer22 = gamePlayerRepository.save(new GamePlayer(game2.getCreationDate(), player2, game2));
			GamePlayer gamePlayer31 = gamePlayerRepository.save(new GamePlayer(game3.getCreationDate(), player2, game3));
			GamePlayer gamePlayer32 = gamePlayerRepository.save(new GamePlayer(game3.getCreationDate(), player4, game3));
			GamePlayer gamePlayer41 = gamePlayerRepository.save(new GamePlayer(game4.getCreationDate(), player2, game4));
			GamePlayer gamePlayer42 = gamePlayerRepository.save(new GamePlayer(game4.getCreationDate(), player1, game4));
			GamePlayer gamePlayer51 = gamePlayerRepository.save(new GamePlayer(game5.getCreationDate(), player4, game5));
			GamePlayer gamePlayer52 = gamePlayerRepository.save(new GamePlayer(game5.getCreationDate(), player1, game5));
			GamePlayer gamePlayer61 = gamePlayerRepository.save(new GamePlayer(game6.getCreationDate(), player3, game6));
			GamePlayer gamePlayer71 = gamePlayerRepository.save(new GamePlayer(game7.getCreationDate(), player4, game7));
			GamePlayer gamePlayer81 = gamePlayerRepository.save(new GamePlayer(game8.getCreationDate(), player3, game8));
			GamePlayer gamePlayer82 = gamePlayerRepository.save(new GamePlayer(game8.getCreationDate(), player4, game8));

			Salvo salvo11 = salvoRepository.save(new Salvo(1L, new HashSet<>(Arrays.asList("B4", "C5", "F1")), gamePlayer11));
			Salvo salvo12 = salvoRepository.save(new Salvo(2L, new HashSet<>(Arrays.asList("F2", "D5")), gamePlayer11));
			gamePlayer11.addSalvo(salvo11);
			gamePlayer12.addSalvo(salvo12);

			Salvo salvo21 = salvoRepository.save(new Salvo(1L, new HashSet<>(Arrays.asList("B4", "B5", "B6")), gamePlayer12));
			Salvo salvo22 = salvoRepository.save(new Salvo(2L, new HashSet<>(Arrays.asList("E1", "H3", "A2")), gamePlayer12));
			gamePlayer12.addSalvo(salvo21);
			gamePlayer12.addSalvo(salvo22);

			Salvo salvo31 = salvoRepository.save(new Salvo(1L, new HashSet<>(Arrays.asList("A2", "A4", "G6")), gamePlayer21));
			Salvo salvo32 = salvoRepository.save(new Salvo(2L, new HashSet<>(Arrays.asList("A3", "H6")), gamePlayer21));
			gamePlayer21.addSalvo(salvo31);
			gamePlayer21.addSalvo(salvo32);

			Salvo salvo41 = salvoRepository.save(new Salvo(1L, new HashSet<>(Arrays.asList("B5", "D5", "C7")), gamePlayer22));
			Salvo salvo42 = salvoRepository.save(new Salvo(2L, new HashSet<>(Arrays.asList("C5", "C6")), gamePlayer22));
			gamePlayer21.addSalvo(salvo41);
			gamePlayer21.addSalvo(salvo42);

			Salvo salvo51 = salvoRepository.save(new Salvo(1L, new HashSet<>(Arrays.asList("G6", "H6", "A4")), gamePlayer31));
			Salvo salvo52 = salvoRepository.save(new Salvo(2L, new HashSet<>(Arrays.asList("A2", "A3", "D8")), gamePlayer31));
			gamePlayer31.addSalvo(salvo51);
			gamePlayer31.addSalvo(salvo52);

			Salvo salvo61 = salvoRepository.save(new Salvo(1L, new HashSet<>(Arrays.asList("H1", "H2", "H3")), gamePlayer32));
			Salvo salvo62 = salvoRepository.save(new Salvo(2L, new HashSet<>(Arrays.asList("E1", "F2", "G3")), gamePlayer32));
			gamePlayer32.addSalvo(salvo61);
			gamePlayer32.addSalvo(salvo62);

			Salvo salvo71 = salvoRepository.save(new Salvo(1L, new HashSet<>(Arrays.asList("A3", "A4", "F7")), gamePlayer41));
			Salvo salvo72 = salvoRepository.save(new Salvo(2L, new HashSet<>(Arrays.asList("A2", "G6", "H6")), gamePlayer41));
			gamePlayer41.addSalvo(salvo71);
			gamePlayer41.addSalvo(salvo72);

			Salvo salvo81 = salvoRepository.save(new Salvo(1L, new HashSet<>(Arrays.asList("B5", "C6", "H1")), gamePlayer42));
			Salvo salvo82 = salvoRepository.save(new Salvo(2L, new HashSet<>(Arrays.asList("C5", "C7", "D5")), gamePlayer42));
			gamePlayer42.addSalvo(salvo81);
			gamePlayer42.addSalvo(salvo82);

			Salvo salvo91 = salvoRepository.save(new Salvo(1L, new HashSet<>(Arrays.asList("A1", "A2", "A3")), gamePlayer51));
			Salvo salvo92 = salvoRepository.save(new Salvo(2L, new HashSet<>(Arrays.asList("G6", "G7", "G8")), gamePlayer51));
			Salvo salvo93 = salvoRepository.save(new Salvo(3L, new HashSet<>(), gamePlayer51));
			gamePlayer51.addSalvo(salvo91);
			gamePlayer51.addSalvo(salvo92);
			gamePlayer51.addSalvo(salvo93);

			Salvo salvo101 = salvoRepository.save(new Salvo(1L, new HashSet<>(Arrays.asList("B5", "B6", "C7")), gamePlayer52));
			Salvo salvo102 = salvoRepository.save(new Salvo(2L, new HashSet<>(Arrays.asList("C6", "D6", "E6")), gamePlayer52));
			Salvo salvo103 = salvoRepository.save(new Salvo(3L, new HashSet<>(Arrays.asList("H1", "H8")), gamePlayer52));
			gamePlayer52.addSalvo(salvo101);
			gamePlayer52.addSalvo(salvo102);
			gamePlayer52.addSalvo(salvo103);

			shipRepository.save(new Ship("Destroyer", new HashSet<>(Arrays.asList("H2", "H3", "H4")), gamePlayer11));
			shipRepository.save(new Ship("Submarine", new HashSet<>(Arrays.asList("E1", "F1", "G1")), gamePlayer11));
			shipRepository.save(new Ship("Patrol Boat", new HashSet<>(Arrays.asList("B4", "B5")), gamePlayer11));
			shipRepository.save(new Ship("Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer12));
			shipRepository.save(new Ship("Patrol Boat", new HashSet<>(Arrays.asList("F1", "F2")), gamePlayer12));
			shipRepository.save(new Ship("Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer21));
			shipRepository.save(new Ship("Patrol Boat", new HashSet<>(Arrays.asList("C6", "C7")), gamePlayer21));
			shipRepository.save(new Ship("Submarine", new HashSet<>(Arrays.asList("A2", "A3", "A4")), gamePlayer22));
			shipRepository.save(new Ship("Patrol Boat ", new HashSet<>(Arrays.asList("G6", "H6")), gamePlayer22));
			shipRepository.save(new Ship("Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer22));
			shipRepository.save(new Ship("Patrol Boat", new HashSet<>(Arrays.asList("C6", "C7")), gamePlayer22));
			shipRepository.save(new Ship("Submarine", new HashSet<>(Arrays.asList("A2", "A3", "A4")), gamePlayer31));
			shipRepository.save(new Ship("Patrol Boat", new HashSet<>(Arrays.asList("G6", "H6")), gamePlayer31));
			shipRepository.save(new Ship("Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer41));
			shipRepository.save(new Ship("Patrol Boat", new HashSet<>(Arrays.asList("G6", "H6")), gamePlayer41));
			shipRepository.save(new Ship("Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer52));
			shipRepository.save(new Ship("Patrol Boat", new HashSet<>(Arrays.asList("C6", "C7")), gamePlayer52));
			shipRepository.save(new Ship("Submarine", new HashSet<>(Arrays.asList("A2", "A3", "A4")), gamePlayer51));
			shipRepository.save(new Ship("Patrol Boat", new HashSet<>(Arrays.asList("G6", "H6")), gamePlayer51));
			shipRepository.save(new Ship("Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer61));
			shipRepository.save(new Ship("Patrol Boat", new HashSet<>(Arrays.asList("C6", "C7")), gamePlayer61));
			shipRepository.save(new Ship("Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer81));
			shipRepository.save(new Ship("Patrol Boat", new HashSet<>(Arrays.asList("C6", "C7")), gamePlayer81));
			shipRepository.save(new Ship("Submarine", new HashSet<>(Arrays.asList("A2", "A3", "A4")), gamePlayer82));
			shipRepository.save(new Ship("Patrol Boat", new HashSet<>(Arrays.asList("G6", "H6")), gamePlayer82));

			Score score11 = scoreRepository.save(new Score(1f, game1.getCreationDate(), game1, player1));
			Score score12 = scoreRepository.save(new Score(0f, game1.getCreationDate(), game1, player2));
			game1.addScore(score11);
			game1.addScore(score12);
			player1.addScore(score11);
			player2.addScore(score12);

			Score score21 = scoreRepository.save(new Score(0.5f, game2.getCreationDate(), game2, player1));
			Score score22 = scoreRepository.save(new Score(0.5f, game2.getCreationDate(), game2, player2));
			game2.addScore(score21);
			game2.addScore(score22);
			player1.addScore(score21);
			player2.addScore(score22);

			Score score31 = scoreRepository.save(new Score(1f, game3.getCreationDate(), game3, player2));
			Score score32 = scoreRepository.save(new Score(0f, game3.getCreationDate(), game3, player4));
			game3.addScore(score31);
			game3.addScore(score32);
			player2.addScore(score31);
			player4.addScore(score32);

			Score score41 = scoreRepository.save(new Score(0.5f, game4.getCreationDate(), game4, player2));
			Score score42 = scoreRepository.save(new Score(0.5f, game4.getCreationDate(), game4, player1));
			game4.addScore(score41);
			game4.addScore(score42);
			player2.addScore(score41);
			player1.addScore(score42);
		};
	}

}