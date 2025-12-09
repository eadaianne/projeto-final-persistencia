package inf.ufg.projeto_final_persistencia.config;

import inf.ufg.projeto_final_persistencia.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupTestConfig {

    @Bean
    CommandLineRunner testDbConnection(UsuarioRepository usuarioRepository) {
        return args -> {
            long count = usuarioRepository.count();
            System.out.println(">>> Banco conectado com sucesso! Total de usuarios = " + count);
        };
    }
}
