package me.demo.jwt.application.post.domain

import javax.persistence.*

@Entity
class Post private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    val memberId: Long,

    @Column(name = "title")
    private var _title: String,

    @Column(name = "description")
    private var _description: String,

    mediaIds: Set<Long>
) {
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "post_media", joinColumns = [JoinColumn(name = "post_id")])
    @Column(name = "media_id")
    private val mediaIds: MutableSet<Long> = hashSetOf()

    val title get() = _title
    val description get() = _description

    init {
        this.mediaIds.addAll(mediaIds)
    }

    constructor(
        memberId: Long,
        title: String,
        description: String,
        mediaIds: Set<Long> = emptySet()
    ) : this(
        id = null,
        memberId = memberId,
        _title = title,
        _description = description,
        mediaIds = mediaIds
    )
}