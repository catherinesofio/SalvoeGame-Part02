package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entities.*;
import com.codeoftheweb.salvo.repositories.*;
import com.codeoftheweb.salvo.utils.GameStates;
import com.codeoftheweb.salvo.utils.PlayerStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, SalvoRepository salvoRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, ScoreRepository scoreRepository){
		return args -> {
			Player player1 = playerRepository.save(new Player("Jack Bauer", "j.bauer@ctu.gov", passwordEncoder.encode("24")));
			Player player2 = playerRepository.save(new Player("Chloe O'Brian", "c.obrian@ctu.gov", passwordEncoder.encode("42")));
			Player player3 = playerRepository.save(new Player("Kim Bauer", "kim_bauer@gmail.com", passwordEncoder.encode("kb")));
			Player player4 = playerRepository.save(new Player("Tony Almeida", "t.almeida@ctu.gov", passwordEncoder.encode("mole")));

			/*Date date = new Date();
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

			gamePlayer61.setState(PlayerStates.WAITING_PREPARING);

			gamePlayer71.setState(PlayerStates.WAITING_PREPARING);

			gamePlayer81.setState(PlayerStates.WAITING_PREPARING);
			gamePlayer82.setState(PlayerStates.WAITING_PREPARING);

			Salvo salvo = salvoRepository.save(new Salvo(1L, "B4", gamePlayer11));
			gamePlayer11.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "C5", gamePlayer11));
			gamePlayer11.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "F1", gamePlayer11));
			gamePlayer11.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(2L, "F2", gamePlayer11));
			gamePlayer12.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "D5", gamePlayer11));
			gamePlayer12.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(1L, "B4", gamePlayer12));
			gamePlayer12.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "B5", gamePlayer12));
			gamePlayer12.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "B6", gamePlayer12));
			gamePlayer12.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(2L, "E1", gamePlayer12));
			gamePlayer12.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "H3", gamePlayer12));
			gamePlayer12.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "A2", gamePlayer12));
			gamePlayer12.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(1L, "A2", gamePlayer21));
			gamePlayer21.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "A4", gamePlayer21));
			gamePlayer21.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "G6", gamePlayer21));
			gamePlayer21.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(2L, "A3", gamePlayer21));
			gamePlayer21.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "H6", gamePlayer21));
			gamePlayer21.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(1L, "B5", gamePlayer22));
			gamePlayer21.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "D5", gamePlayer22));
			gamePlayer21.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "C7", gamePlayer22));
			gamePlayer21.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(2L, "C5", gamePlayer22));
			gamePlayer21.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(2L, "C6", gamePlayer22));
			gamePlayer21.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(1L, "G6", gamePlayer31));
			gamePlayer31.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "H6", gamePlayer31));
			gamePlayer31.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "A4", gamePlayer31));
			gamePlayer31.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(2L, "A2", gamePlayer31));
			gamePlayer31.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "A3", gamePlayer31));
			gamePlayer31.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "D8", gamePlayer31));
			gamePlayer31.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(1L, "H1", gamePlayer32));
			gamePlayer32.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "H2", gamePlayer32));
			gamePlayer32.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "H3", gamePlayer32));
			gamePlayer32.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(2L, "E1", gamePlayer32));
			gamePlayer32.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "F2", gamePlayer32));
			gamePlayer32.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "G3", gamePlayer32));
			gamePlayer32.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(1L, "A3", gamePlayer41));
			gamePlayer41.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "A4", gamePlayer41));
			gamePlayer41.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "F7", gamePlayer41));
			gamePlayer41.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(2L, "A2", gamePlayer41));
			gamePlayer41.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "G6", gamePlayer41));
			gamePlayer41.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "H6", gamePlayer41));
			gamePlayer41.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(1L, "B5", gamePlayer42));
			gamePlayer42.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "C6", gamePlayer42));
			gamePlayer42.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "H1", gamePlayer42));
			gamePlayer42.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(2L, "C5", gamePlayer42));
			gamePlayer42.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "C7", gamePlayer42));
			gamePlayer42.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "D5", gamePlayer42));
			gamePlayer42.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(1L, "A1", gamePlayer51));
			gamePlayer51.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "A2", gamePlayer51));
			gamePlayer51.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "A3", gamePlayer51));
			gamePlayer51.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(2L, "G6", gamePlayer51));
			gamePlayer51.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "G7", gamePlayer51));
			gamePlayer51.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "G8", gamePlayer51));
			gamePlayer51.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(1L, "B5", gamePlayer52));
			gamePlayer52.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "B6", gamePlayer52));
			gamePlayer52.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(1L, "C7", gamePlayer52));
			gamePlayer52.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(2L, "C6", gamePlayer52));
			gamePlayer52.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "D6", gamePlayer52));
			gamePlayer52.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(2L, "E6", gamePlayer52));
			gamePlayer52.addSalvo(salvo);

			salvo = salvoRepository.save(new Salvo(3L, "H1", gamePlayer52));
			gamePlayer52.addSalvo(salvo);
			salvo = salvoRepository.save(new Salvo(3L, "H8", gamePlayer52));
			gamePlayer52.addSalvo(salvo);

			gamePlayer51.setState(PlayerStates.PLAYING_TURN);
			gamePlayer52.setState(PlayerStates.PLAYING_WAITING);
			game5.setState(GameStates.PLAYING);

			shipRepository.save(new Ship(ShipTypes.DESTROYER, new HashSet<>(Arrays.asList("H2", "H3", "H4")), gamePlayer11));
			shipRepository.save(new Ship(ShipTypes.SUBMARINE, new HashSet<>(Arrays.asList("E1", "F1", "G1")), gamePlayer11));
			shipRepository.save(new Ship(ShipTypes.PATROL_BOAT, new HashSet<>(Arrays.asList("B4", "B5")), gamePlayer11));
			shipRepository.save(new Ship(ShipTypes.DESTROYER, new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer12));
			shipRepository.save(new Ship(ShipTypes.PATROL_BOAT, new HashSet<>(Arrays.asList("F1", "F2")), gamePlayer12));
			shipRepository.save(new Ship(ShipTypes.DESTROYER, new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer21));
			shipRepository.save(new Ship(ShipTypes.PATROL_BOAT, new HashSet<>(Arrays.asList("C6", "C7")), gamePlayer21));
			shipRepository.save(new Ship(ShipTypes.SUBMARINE, new HashSet<>(Arrays.asList("A2", "A3", "A4")), gamePlayer22));
			shipRepository.save(new Ship(ShipTypes.PATROL_BOAT, new HashSet<>(Arrays.asList("G6", "H6")), gamePlayer22));
			shipRepository.save(new Ship(ShipTypes.DESTROYER, new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer22));
			shipRepository.save(new Ship(ShipTypes.PATROL_BOAT, new HashSet<>(Arrays.asList("C6", "C7")), gamePlayer22));
			shipRepository.save(new Ship(ShipTypes.SUBMARINE, new HashSet<>(Arrays.asList("A2", "A3", "A4")), gamePlayer31));
			shipRepository.save(new Ship(ShipTypes.PATROL_BOAT, new HashSet<>(Arrays.asList("G6", "H6")), gamePlayer31));
			shipRepository.save(new Ship(ShipTypes.DESTROYER, new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer41));
			shipRepository.save(new Ship(ShipTypes.PATROL_BOAT, new HashSet<>(Arrays.asList("G6", "H6")), gamePlayer41));
			shipRepository.save(new Ship(ShipTypes.DESTROYER, new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer52));
			shipRepository.save(new Ship(ShipTypes.PATROL_BOAT, new HashSet<>(Arrays.asList("C6", "C7")), gamePlayer52));
			shipRepository.save(new Ship(ShipTypes.SUBMARINE, new HashSet<>(Arrays.asList("A2", "A3", "A4")), gamePlayer51));
			shipRepository.save(new Ship(ShipTypes.PATROL_BOAT, new HashSet<>(Arrays.asList("G6", "H6")), gamePlayer51));
			shipRepository.save(new Ship(ShipTypes.DESTROYER, new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer61));
			shipRepository.save(new Ship(ShipTypes.PATROL_BOAT, new HashSet<>(Arrays.asList("C6", "C7")), gamePlayer61));
			shipRepository.save(new Ship(ShipTypes.DESTROYER, new HashSet<>(Arrays.asList("B5", "C5", "D5")), gamePlayer81));
			shipRepository.save(new Ship(ShipTypes.PATROL_BOAT, new HashSet<>(Arrays.asList("C6", "C7")), gamePlayer81));
			shipRepository.save(new Ship(ShipTypes.SUBMARINE, new HashSet<>(Arrays.asList("A2", "A3", "A4")), gamePlayer82));
			shipRepository.save(new Ship(ShipTypes.PATROL_BOAT, new HashSet<>(Arrays.asList("G6", "H6")), gamePlayer82));

			Score score11 = scoreRepository.save(new Score(1f, game1.getCreationDate(), game1, player1));
			Score score12 = scoreRepository.save(new Score(0f, game1.getCreationDate(), game1, player2));
			game1.addScore(score11);
			game1.addScore(score12);
			player1.addScore(score11);
			player2.addScore(score12);
			gamePlayer11.setState(PlayerStates.FINISHED_WON);
			gamePlayer12.setState(PlayerStates.FINISHED_LOST);
			game1.setState(GameStates.FINISHED);

			Score score21 = scoreRepository.save(new Score(0.5f, game2.getCreationDate(), game2, player1));
			Score score22 = scoreRepository.save(new Score(0.5f, game2.getCreationDate(), game2, player2));
			game2.addScore(score21);
			game2.addScore(score22);
			player1.addScore(score21);
			player2.addScore(score22);
			gamePlayer21.setState(PlayerStates.FINISHED_TIED);
			gamePlayer22.setState(PlayerStates.FINISHED_TIED);
			game1.setState(GameStates.FINISHED);

			Score score31 = scoreRepository.save(new Score(1f, game3.getCreationDate(), game3, player2));
			Score score32 = scoreRepository.save(new Score(0f, game3.getCreationDate(), game3, player4));
			game3.addScore(score31);
			game3.addScore(score32);
			player2.addScore(score31);
			player4.addScore(score32);
			gamePlayer31.setState(PlayerStates.FINISHED_WON);
			gamePlayer32.setState(PlayerStates.FINISHED_LOST);
			game1.setState(GameStates.FINISHED);

			Score score41 = scoreRepository.save(new Score(0.5f, game4.getCreationDate(), game4, player2));
			Score score42 = scoreRepository.save(new Score(0.5f, game4.getCreationDate(), game4, player1));
			game4.addScore(score41);
			game4.addScore(score42);
			player2.addScore(score41);
			player1.addScore(score42);
			gamePlayer41.setState(PlayerStates.FINISHED_TIED);
			gamePlayer42.setState(PlayerStates.FINISHED_TIED);
			game1.setState(GameStates.FINISHED);

			gamePlayerRepository.save(gamePlayer11);
			gamePlayerRepository.save(gamePlayer12);
			gamePlayerRepository.save(gamePlayer21);
			gamePlayerRepository.save(gamePlayer22);
			gamePlayerRepository.save(gamePlayer31);
			gamePlayerRepository.save(gamePlayer32);
			gamePlayerRepository.save(gamePlayer41);
			gamePlayerRepository.save(gamePlayer42);
			gamePlayerRepository.save(gamePlayer51);
			gamePlayerRepository.save(gamePlayer52);
			gamePlayerRepository.save(gamePlayer61);
			gamePlayerRepository.save(gamePlayer71);
			gamePlayerRepository.save(gamePlayer81);
			gamePlayerRepository.save(gamePlayer82);

			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
			gameRepository.save(game5);
			gameRepository.save(game6);
			gameRepository.save(game7);
			gameRepository.save(game8);*/
		};
	}

}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	private PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService( email -> {
			Player player = playerRepository.findByEmail(email);

			if (player != null) {
				return new User(player.getEmail(), player.getPassword(), AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + email);
			}
		});
	}

}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/rest/**").denyAll()
				.antMatchers("/api/game_view/**").hasAuthority("USER")
				.antMatchers("/**").permitAll()
				.anyRequest().permitAll();

		http.formLogin()
				.usernameParameter("email")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout").permitAll();

		http.headers().frameOptions().sameOrigin();

		http.csrf().disable();

		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		http.formLogin().successHandler((req, res, exc) -> clearAuthenticationAttributes(req));

		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}