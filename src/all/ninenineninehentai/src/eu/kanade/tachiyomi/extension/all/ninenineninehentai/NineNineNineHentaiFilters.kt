package eu.kanade.tachiyomi.extension.all.ninenineninehentai

import eu.kanade.tachiyomi.source.model.Filter
import eu.kanade.tachiyomi.source.model.FilterList

abstract class SelectFilter(
    displayName: String,
    private val options: Array<Pair<String, String>>,
) : Filter.Select<String>(
    displayName,
    options.map { it.first }.toTypedArray(),
) {
    val selected get() = options[state].second.takeUnless { it.isEmpty() }
}

abstract class TextFilter(name: String) : Filter.Text(name)

abstract class TagFilter(name: String) : TextFilter(name) {
    val tags get() = state.split(",")
        .map { it.trim().lowercase() }
        .filter { it.isNotEmpty() }
        .takeUnless { it.isEmpty() }
}

abstract class PageFilter(name: String) : TextFilter(name) {
    val value get() = state.trim().toIntOrNull()
}

class SortFilter : SelectFilter(
    "Sort By",
    arrayOf(
        Pair("Update", ""),
        Pair("Popular", "Popular"),
        Pair("Name Ascending", "Name_ASC"),
        Pair("Name Descending", "Name_DESC"),
    ),
)

class FormatFilter : SelectFilter(
    "Format",
    arrayOf(
        Pair("", ""),
        Pair("Manga", "manga"),
        Pair("Doujinshi", "doujinshi"),
        Pair("ArtistCG", "artistcg"),
        Pair("GameCG", "gamecg"),
        Pair("ImageSet", "imageset"),
    ),
)

class MinPageFilter : PageFilter("Minimum Pages")

class MaxPageFilter : PageFilter("Maximum Pages")

class IncludedTagFilter : SelectFilter(
    "Include Tag",
    genrePairs.distinct().sortedBy { it.first }.toTypedArray(),
)
class ExcludedTagFilter : TagFilter("Exclude Tags")

private const val GENRE_TAGS = ",Uncensored,Futanari,Shotacon,Lolicon,Incest,Loli,Mind Control,Netorare,Milf,Rape,Bestiality,Yaoi,Yuri,Anal,Mother,Dark Skin,Tentacles,Sex Toys,Sole Female,Impregnation,Pregnant,Big Breasts,Gender Bender,Bondage,Femdom,Monster,Crossdressing,Harem,Lactation,Cheating,Ahegao,Huge Breasts,Tomgirl,Guro,Bbm,Feminization,Exhibitionism,Gyaru,Elf,Teacher,Masturbation,Stockings,Bdsm,Shota,Blackmail,Public Sex,Schoolgirl Uniform,Prostitution,Maid,Swimsuit,Blowjob,Group,Hairy,Paizuri,Torture,Vtuber"

val genrePairs = GENRE_TAGS.split(",").map { it to it.lowercase() }.toTypedArray()

fun getFilters() = FilterList(
    SortFilter(),
    FormatFilter(),
    Filter.Separator(),
    MinPageFilter(),
    MaxPageFilter(),
    Filter.Separator(),
    IncludedTagFilter(),
    ExcludedTagFilter(),
    Filter.Header("comma (,) separated tag/parody/character/artist/group"),
)


