package com.example.playstoreapp

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class SponsoredApp(
    val name: String,
    val category: String,
    val rating: String,
    val size: String,
    val iconColor: Int,
    var isStarred: Boolean = false
)

data class RecommendedApp(
    val name: String,
    val iconColor: Int
)

class MainActivity : AppCompatActivity() {
    private lateinit var llSponsoredContainer: LinearLayout
    private lateinit var rvRecommended: RecyclerView

    private val sponsoredList = mutableListOf<SponsoredApp>()
    private val recommendedList = mutableListOf<RecommendedApp>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        loadSampleData()
        setupSponsoredSection()
        setupRecommendedSection()
    }

    private fun initializeViews() {
        llSponsoredContainer = findViewById(R.id.llSponsoredContainer)
        rvRecommended = findViewById(R.id.rvRecommended)
    }

    private fun loadSampleData() {
        sponsoredList.addAll(listOf(
            SponsoredApp(
                "Mech Assemble: Zombie Swarm",
                "Action • Role Playing • Roguelike • Zombie",
                "4.8",
                "624 MB",
                Color.parseColor("#8B0000")
            ),
            SponsoredApp(
                "MU: Hồng Hoá Bạo",
                "Role Playing",
                "4.8",
                "339 MB",
                Color.parseColor("#8B4513")
            ),
            SponsoredApp(
                "War Inc: Rising",
                "Strategy • Tower defense",
                "4.9",
                "231 MB",
                Color.parseColor("#FFD700")
            ),
            SponsoredApp(
                "Battle Royale 2025",
                "Action • Shooter • Multiplayer",
                "4.7",
                "512 MB",
                Color.parseColor("#2E86AB")
            ),
            SponsoredApp(
                "Racing Speed",
                "Racing • Simulation",
                "4.6",
                "428 MB",
                Color.parseColor("#A23B72")
            ),
            SponsoredApp(
                "Puzzle Master",
                "Puzzle • Brain Training",
                "4.8",
                "156 MB",
                Color.parseColor("#F18F01")
            ),
            SponsoredApp(
                "Space Adventure",
                "Adventure • Sci-fi",
                "4.5",
                "890 MB",
                Color.parseColor("#6A4C93")
            )
        ))

        recommendedList.addAll(listOf(
            RecommendedApp("Suno - AI Music & Songs", Color.parseColor("#FF6B35")),
            RecommendedApp("Claude by Anthropic", Color.parseColor("#FF8C42")),
            RecommendedApp("DramaBox - Short Drama", Color.parseColor("#FF4757"))
        ))
    }

    private fun setupSponsoredSection() {
        val itemsPerColumn = 3
        val columns = sponsoredList.chunked(itemsPerColumn)

        columns.forEachIndexed { index, columnApps ->
            // Tạo một cột dọc
            val columnLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    resources.displayMetrics.widthPixels - 32.dpToPx(), // Full width minus padding
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    if (index > 0) marginStart = 8.dpToPx()
                }
            }

            columnApps.forEach { app ->
                val itemView = LayoutInflater.from(this)
                    .inflate(R.layout.item_sponsored_app, columnLayout, false)

                // Bind data
                val ivAppIcon = itemView.findViewById<ImageView>(R.id.ivAppIcon)
                val tvAppName = itemView.findViewById<TextView>(R.id.tvAppName)
                val tvCategory = itemView.findViewById<TextView>(R.id.tvCategory)
                val tvRating = itemView.findViewById<TextView>(R.id.tvRating)
                val tvSize = itemView.findViewById<TextView>(R.id.tvSize)
                val btnStar = itemView.findViewById<ImageButton>(R.id.btnStar)

                ivAppIcon.setBackgroundColor(app.iconColor)
                tvAppName.text = app.name
                tvCategory.text = app.category
                tvRating.text = app.rating
                tvSize.text = app.size

                updateStarIcon(btnStar, app.isStarred)

                btnStar.setOnClickListener {
                    app.isStarred = !app.isStarred
                    updateStarIcon(btnStar, app.isStarred)

                    val message = if (app.isStarred) {
                        "Added to favorites: ${app.name}"
                    } else {
                        "Removed from favorites: ${app.name}"
                    }
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }

                itemView.setOnClickListener {
                    Toast.makeText(this, "Clicked: ${app.name}", Toast.LENGTH_SHORT).show()
                }

                columnLayout.addView(itemView)
            }

            llSponsoredContainer.addView(columnLayout)
        }
    }

    // Helper function to convert dp to pixels
    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun setupRecommendedSection() {
        rvRecommended.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvRecommended.adapter = RecommendedAdapter(recommendedList)
    }

    private fun updateStarIcon(button: ImageButton, isStarred: Boolean) {
        if (isStarred) {
            button.setImageResource(android.R.drawable.btn_star_big_on)
            button.setColorFilter(Color.parseColor("#FBBC04"))
        } else {
            button.setImageResource(android.R.drawable.btn_star_big_off)
            button.setColorFilter(Color.parseColor("#5F6368"))
        }
    }

    // Recommended Adapter
    inner class RecommendedAdapter(private val apps: List<RecommendedApp>) :
        RecyclerView.Adapter<RecommendedAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivAppIcon: ImageView = view.findViewById(R.id.ivAppIcon)
            val tvAppName: TextView = view.findViewById(R.id.tvAppName)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recommended_app, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val app = apps[position]

            holder.ivAppIcon.setBackgroundColor(app.iconColor)
            holder.tvAppName.text = app.name

            holder.itemView.setOnClickListener {
                Toast.makeText(this@MainActivity, "Clicked: ${app.name}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun getItemCount() = apps.size
    }
}