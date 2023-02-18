package uz.sh;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@OpenAPIDefinition
@SpringBootApplication
public class ClickHouseDemoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClickHouseDemoAppApplication.class, args);
    }

//    @Bean
//    public JdbcTemplate jdbcTemplate() {
//        return new JdbcTemplate();
//    }
}

//@Configuration
//class DbConfiguration : AbstractJdbcConfiguration() {
//@Bean
//    override fun jdbcCustomConversions(): JdbcCustomConversions {
//            return JdbcCustomConversions(
//            mutableListOf(
//            StringListWritingConverter(),
//            StringListReadingConverter()
//            )
//            )
//            }
//            }

//@Configuration
//class DbConfiguration extends AbstractJdbcConfiguration {
//
//    private final StringListWritingConverter stringListWritingConverter;
//    private final StringListReadingConverter stringListReadingConverter;
//
//    DbConfiguration(StringListWritingConverter stringListWritingConverter, StringListReadingConverter stringListReadingConverter) {
//        this.stringListWritingConverter = stringListWritingConverter;
//        this.stringListReadingConverter = stringListReadingConverter;
//    }
//
//    @Override
//    public JdbcCustomConversions jdbcCustomConversions() {
//        return new JdbcCustomConversions(List.of(stringListWritingConverter, stringListReadingConverter));
//    }
//}

@RestController
@RequestMapping( "/user" )
class UserjonController {

    private final UserJonRepoImpl userJonRepo;

    UserjonController(UserJonRepoImpl userJonRepo) {
        this.userJonRepo = userJonRepo;
    }

    @PostMapping( "/create" )
    public Userjon create(@RequestBody Userjon userJon) {
        userJon.setId(UUID.randomUUID().toString());
        Userjon save = userJonRepo.save(userJon);
        return save;
    }

    @GetMapping( "/getAll" )
    public List<Userjon> getAll() {
        return userJonRepo.findAll();
    }

    @GetMapping( "/getById/{id}" )
    public Userjon getById(@PathVariable( "id" ) String id) {
        return userJonRepo.findById(id);
    }
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Userjon {

    private String id;

    private String username;

    private String password;


}


interface UserJonRepo {

    Userjon findById(String id);

    List<Userjon> findAll();

    Userjon save(Userjon userjon);
}

@Repository
class UserJonRepoImpl implements UserJonRepo {

    private final JdbcTemplate jdbcTemplate;

    UserJonRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Userjon mapRowToUserJon(ResultSet row, int rowNum)
            throws SQLException {
        return new Userjon(
                row.getString("id"),
                row.getString("username"),
                row.getString("password")
        );
    }


    @Override
    public Userjon findById(String id) {

        List<Userjon> list = jdbcTemplate.query("select u.* from user_jon u where u.id = ?", this::mapRowToUserJon, id);
        return list.size() == 0 ?
                null :
                list.get(0);
    }

    @Override
    public List<Userjon> findAll() {
        List<Userjon> list = jdbcTemplate.query("select u.* from user_jon u ", this::mapRowToUserJon);
        return list;
    }

    @Override
    public Userjon save(Userjon userjon) {
        jdbcTemplate.update(
                "insert into user_jon (id, username, password) values (?, ?, ?)",
                userjon.getId(),
                userjon.getUsername(),
                userjon.getPassword());
        return userjon;
    }
}

//source.list.joinToString(",", "[", "]") { "'$it'" }

//@Component
//@WritingConverter
//class StringListWritingConverter implements Converter<List<String>, String> {
//    @Override
//    public String convert(List<String> source) {
//        String str = "[";
//        for ( String f : source ) {
//            str = str + f + ",";
//        }
//        int i = str.lastIndexOf(",");
//        String substring = str.substring(0, str.length() - 1);
//        return substring + "]";
//    }
//}
//
//@Component
//@ReadingConverter
//class StringListReadingConverter implements Converter<String, List<String>> {
//    @Override
//    public List<String> convert(String source) {
//        String substring = source.substring(1, source.length() - 1);
//        String replace = substring.replace("'", "");
//        String[] split = replace.split(",");
//        return Arrays.asList(split);
//    }
//    override fun
//
//    convert(source:String):
//
//    StringList {
//        val list = source.removePrefix("[").removeSuffix("]").replace("'", "").split(",")
//        return StringList(list)
//    }


