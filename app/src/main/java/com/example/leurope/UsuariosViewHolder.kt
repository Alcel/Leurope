package www.iesmurgi.u9_proyprofesoressqlite

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.leurope.databinding.UsuariosEsqueletoBinding

class UsuariosViewHolder (vista: View):RecyclerView.ViewHolder(vista){
  //  private val miBinding=UsuariosLayoutBinding.bind(vista)
    private val miBinding= UsuariosEsqueletoBinding.bind(vista)
    fun inflar(profesor:Usuarios,
        onItemDelete:(Int)->Unit,
        onItemUpdate:(Usuarios)->Unit)
    {

        miBinding.tvNombre.text=profesor.nombre
        miBinding.btnBorrar.setOnClickListener{
        onItemDelete(adapterPosition)
        }
        itemView.setOnClickListener { onItemUpdate(profesor) }
    }
}