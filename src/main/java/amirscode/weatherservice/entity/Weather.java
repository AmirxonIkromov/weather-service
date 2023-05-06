package amirscode.weatherservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float temp;
    private Float windSpeed;
    private Integer pressure;
    private Integer humidity;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updateAt;

    public Weather(Long id, Float temp, Float windSpeed, Integer pressure, Integer humidity) {
        this.id = id;
        this.temp = temp;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.humidity = humidity;
    }
}
