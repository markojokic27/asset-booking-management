package de.bdr.asset.management.asset;


@Entity
@Table (name = "asset")
@Getter
@Setter
@Builder
public class Asset {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long Id

    @NotBlank(message="Name is required")
    @Size(max=100, message="Name cannot exceed 100 characters")
    @Column(nullable=false)
    private String name;

    @Size(max=255, message="Description cannot exceed 255 characters")
    @Column(columnDefinition="TEXT")
    private String description;

    @NotNull(message = "Status is required")
    @Pattern(regexp = "", message = "Status must be ")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    

    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime createdTimestamp;

    @UpdateTimestamp
    private LocalDateTime lastModifiedTimestamp;

}
