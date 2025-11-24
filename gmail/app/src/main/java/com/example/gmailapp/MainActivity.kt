package com.example.gmailapp

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

data class Email(
    val sender: String,
    val subject: String,
    val preview: String,
    val time: String,
    val avatarLetter: String,
    val avatarColor: Int,
    var isStarred: Boolean = false,
    var isImportant: Boolean = false
)

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var infoBanner: LinearLayout
    private lateinit var btnCloseBanner: ImageButton
    private lateinit var fabCompose: com.google.android.material.floatingactionbutton.FloatingActionButton

    private val emailList = mutableListOf<Email>()
    private lateinit var adapter: EmailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        loadSampleEmails()
        setupListView()
        setupButtons()
    }

    private fun initializeViews() {
        listView = findViewById(R.id.listView)
        infoBanner = findViewById(R.id.infoBanner)
        btnCloseBanner = findViewById(R.id.btnCloseBanner)
        fabCompose = findViewById(R.id.fabCompose)
    }

    private fun loadSampleEmails() {
        emailList.addAll(listOf(
            Email(
                "Edurila.com",
                "\$19 Only (First 10 spots) - Bestselling...",
                "Are you looking to Learn Web Designin...",
                "12:34 PM",
                "E",
                Color.parseColor("#4285F4"),
                isImportant = true
            ),
            Email(
                "Chris Abad",
                "Help make Campaign Monitor better",
                "Let us know your thoughts! No Images...",
                "11:22 AM",
                "C",
                Color.parseColor("#EA4335")
            ),
            Email(
                "Tuto.com",
                "8h de formation gratuite et les nouvea...",
                "Photoshop, SEO, Blender, CSS, WordPre...",
                "11:04 AM",
                "T",
                Color.parseColor("#34A853"),
                isImportant = true
            ),
            Email(
                "support",
                "Société Ovh : suivi de vos services - hp...",
                "SAS OVH - http://www.ovh.com 2 rue K...",
                "10:26 AM",
                "S",
                Color.parseColor("#9AA0A6")
            ),
            Email(
                "Matt from Ionic",
                "The New Ionic Creator Is Here!",
                "Announcing the all-new Creator, build...",
                "9:45 AM",
                "M",
                Color.parseColor("#34A853"),
                isImportant = true
            ),
            Email(
                "Matt from Ionic",
                "The New Ionic Creator Is Here!",
                "Announcing the all-new Creator, build...",
                "9:45 AM",
                "M",
                Color.parseColor("#34A853"),
                isImportant = true
            ),
            Email(
                "Matt from Ionic",
                "The New Ionic Creator Is Here!",
                "Announcing the all-new Creator, build...",
                "9:45 AM",
                "M",
                Color.parseColor("#34A853"),
                isImportant = true
            ),
            Email(
                "Matt from Ionic",
                "The New Ionic Creator Is Here!",
                "Announcing the all-new Creator, build...",
                "9:45 AM",
                "M",
                Color.parseColor("#34A853"),
                isImportant = true
            ),
            Email(
                "Matt from Ionic",
                "The New Ionic Creator Is Here!",
                "Announcing the all-new Creator, build...",
                "9:45 AM",
                "M",
                Color.parseColor("#34A853"),
                isImportant = true
            ),
            Email(
                "Matt from Ionic",
                "The New Ionic Creator Is Here!",
                "Announcing the all-new Creator, build...",
                "9:45 AM",
                "M",
                Color.parseColor("#34A853"),
                isImportant = true
            )
        ))
    }

    private fun setupListView() {
        adapter = EmailAdapter()
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(this, "Clicked: ${emailList[position].sender}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupButtons() {
        btnCloseBanner.setOnClickListener {
            infoBanner.visibility = View.GONE
        }

        fabCompose.setOnClickListener {
            Toast.makeText(this, "Compose new email", Toast.LENGTH_SHORT).show()
        }
    }

    inner class EmailAdapter : BaseAdapter() {
        override fun getCount(): Int = emailList.size

        override fun getItem(position: Int): Any = emailList[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View
            val holder: ViewHolder

            if (convertView == null) {
                view = LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.item_email, parent, false)
                holder = ViewHolder(
                    view.findViewById(R.id.tvAvatar),
                    view.findViewById(R.id.ivImportant),
                    view.findViewById(R.id.tvSender),
                    view.findViewById(R.id.tvTime),
                    view.findViewById(R.id.tvSubject),
                    view.findViewById(R.id.tvPreview),
                    view.findViewById(R.id.btnStar)
                )
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }

            val email = emailList[position]

            // Set avatar với hình tròn
            holder.tvAvatar.text = email.avatarLetter

            // Tạo background tròn với màu động
            val drawable = android.graphics.drawable.GradientDrawable()
            drawable.shape = android.graphics.drawable.GradientDrawable.OVAL
            drawable.setColor(email.avatarColor)
            holder.tvAvatar.background = drawable

            // Set important indicator
            holder.ivImportant.visibility = if (email.isImportant) View.VISIBLE else View.GONE

            // Set email info
            holder.tvSender.text = email.sender
            holder.tvTime.text = email.time
            holder.tvSubject.text = email.subject
            holder.tvPreview.text = email.preview

            // Set star icon
            updateStarIcon(holder.btnStar, email.isStarred)

            // Handle star click
            holder.btnStar.setOnClickListener {
                email.isStarred = !email.isStarred
                updateStarIcon(holder.btnStar, email.isStarred)

                val message = if (email.isStarred) {
                    "Added star to ${email.sender}"
                } else {
                    "Removed star from ${email.sender}"
                }
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }

            return view
        }

        private fun updateStarIcon(button: ImageButton, isStarred: Boolean) {
            if (isStarred) {
                button.setImageResource(android.R.drawable.btn_star_big_on)
                button.setColorFilter(Color.parseColor("#F9AB00"))
            } else {
                button.setImageResource(android.R.drawable.btn_star_big_off)
                button.setColorFilter(Color.parseColor("#5F6368"))
            }
        }
    }

    class ViewHolder(
        val tvAvatar: TextView,
        val ivImportant: ImageView,
        val tvSender: TextView,
        val tvTime: TextView,
        val tvSubject: TextView,
        val tvPreview: TextView,
        val btnStar: ImageButton
    )
}