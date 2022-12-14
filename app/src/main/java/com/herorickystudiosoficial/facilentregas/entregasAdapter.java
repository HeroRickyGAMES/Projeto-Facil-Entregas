package com.herorickystudiosoficial.facilentregas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class entregasAdapter extends RecyclerView.Adapter<entregasAdapter.MyViewHolder> {

    Context context;
    ArrayList<cardsEntregas> list;
    private FirebaseFirestore usersDb;
    String UID, entregadorName, statusdaentrega, entregadorNameFromEntrega, latitude, longitude, LocalEntregaa , localizaçãoloja, localizacaoEntregador, preco, uidEntregador, publicKeyEntregador, secretKeyEntregador, precorestante, latitudeEntregador, logitudeEntregador, statusDoProduto, RazaodoEntregador, LocalizacaoEntregador, LatitudeDoEntregador, logitudeDoEntregador;
    Double latiInt, longInt;

    public entregasAdapter(Context context, ArrayList<cardsEntregas> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_entregas,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        cardsEntregas users = list.get(position);

        holder.textTitulo.setText(users.getTitulo());
        holder.textnomeL.setText(users.getNome());
        holder.textlocalL.setText(users.getLocal());
        holder.textendereçoL.setText(users.getLocalentrega());
        holder.textDistanciadvc.setText(users.getTextDistanciadvc());
        holder.textPreco.setText(users.getPreco());
        holder.textestaAtivo.setText(users.getEstaAtivo());
        holder.textEntreguePor.setText(users.getEntreguePor());
        holder.textuidEntregaor.setText(users.getUidEntregaor());
        holder.textProductID.setText(users.getProductID());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textTitulo, textnomeL, textlocalL, textendereçoL, textDistanciadvc, textPreco, textestaAtivo, textEntreguePor, textuidEntregaor, textProductID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            textTitulo = itemView.findViewById(R.id.textTitulo);
            textnomeL = itemView.findViewById(R.id.textProduto);
            textlocalL = itemView.findViewById(R.id.textLocal);
            textendereçoL = itemView.findViewById(R.id.textLocalEntrega);
            textDistanciadvc = itemView.findViewById(R.id.textDistanciadvc);
            textPreco = itemView.findViewById(R.id.textPreco);
            textestaAtivo = itemView.findViewById(R.id.textestaAtivo);
            textEntreguePor = itemView.findViewById(R.id.textEntreguePor);
            textuidEntregaor = itemView.findViewById(R.id.textuidEntregaor);
            textProductID = itemView.findViewById(R.id.textProductID);

        }

        @Override
        public void onClick(View v) {

            usersDb = FirebaseFirestore.getInstance();

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            UID = user.getUid();

            System.out.println(UID);

            DocumentReference entregaDoc =  usersDb.collection("Solicitacoes-Entregas").document(textProductID.getText().toString().replace(" ", ""));

            entregaDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    DocumentSnapshot document = task.getResult();

                    if(document.exists()){
                    statusdaentrega = document.getString("statusDoProduto");
                    entregadorNameFromEntrega = document.getString("entreguePor");
                    uidEntregador = document.getString("uidEntregador");
                    preco = document.getString("Preço");


            DocumentReference lojaDoc =  usersDb.collection("Loja").document(UID);

            lojaDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document2 = task.getResult();

                    if(document2.exists()){

                    if(statusdaentrega.equals("Em Pagamento")){

                        DocumentReference entregaDoc =  usersDb.collection("Entregador").document(uidEntregador);

                        entregaDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                Intent intent = new Intent(context, paymentDeveloper.class);

                                DocumentSnapshot document3 = task.getResult();

                                publicKeyEntregador = document3.getString("publicKey");
                                secretKeyEntregador = document3.getString("secretKey");

                                intent.putExtra("Preco", preco);
                                intent.putExtra("Entregador", entregadorNameFromEntrega);
                                intent.putExtra("PublicKeyEntregador", publicKeyEntregador);
                                intent.putExtra("SecretKeyEntregador", secretKeyEntregador);
                                intent.putExtra("UidEntregador", uidEntregador);
                                intent.putExtra("idProduto", textProductID.getText().toString());
                                intent.putExtra("tituloProduto", textnomeL.getText().toString());
                                context.startActivity(intent);

                            }
                        });
                 }

                    if(statusdaentrega.equals("Entregue")){

                        statusDoProduto = document.getString("statusDoProduto");
                        RazaodoEntregador = document.getString("RazãodoEntregador");
                        LocalizacaoEntregador = document.getString("LocalizacaoEntregador");
                        LatitudeDoEntregador = document.getString("LatitudeDoEntregador");
                        logitudeDoEntregador = document.getString("logitudeEntregador");

                        new AlertDialog.Builder(context)
                                .setTitle("Relatorio de entrega.")
                                .setMessage("Status do produto: " + statusDoProduto + " \n " + "Razão do entregador: " + RazaodoEntregador + " \n " + "Localização do entregador quando fez a entrega: " + LocalizacaoEntregador + " \n "+ "Latitude do entregador quando fez a entrega: " + LatitudeDoEntregador + " \n "+ "Longitude do Entregador quando fez a entrega: " + logitudeDoEntregador)
                                .setCancelable(true)
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();

                    }

                    if(statusdaentrega.equals("Pagas todas as taxas")){

                        precorestante = document.getString("PrecoRestante");

                        DocumentReference entregaDoc =  usersDb.collection("Entregador").document(uidEntregador);

                        entregaDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                DocumentSnapshot document3 = task.getResult();

                                publicKeyEntregador = document3.getString("publicKey");
                                secretKeyEntregador = document3.getString("secretKey");

                                Intent intent2 = new Intent(context, paymentComplete.class);

                                intent2.putExtra("PrecoRestante", precorestante);
                                intent2.putExtra("UidEntregador",  uidEntregador);
                                intent2.putExtra("idProduto",  textProductID.getText().toString());
                                intent2.putExtra("tituloProduto",  textnomeL.getText().toString());
                                intent2.putExtra("PublicKeyEntregador",  publicKeyEntregador);
                                intent2.putExtra("SecretKeyEntregador",  secretKeyEntregador);

                                context.startActivity(intent2);

                            }
                        });
                    }

                    if(statusdaentrega.equals("A caminho")){

                        statusDoProduto = document.getString("statusDoProduto");
                        RazaodoEntregador = document.getString("RazãodoEntregador");
                        LocalizacaoEntregador = document.getString("LocalizacaoEntregador");
                        LatitudeDoEntregador = document.getString("LatitudeDoEntregador");
                        logitudeDoEntregador = document.getString("logitudeEntregador");

                        new AlertDialog.Builder(context)
                                .setTitle("Relatorio de entrega.")
                                .setMessage("Status do produto: " + statusDoProduto + " \n " + "Razão do entregador: " + RazaodoEntregador + " \n " + "Localização do entregador: " + LocalizacaoEntregador + " \n "+ "Latitude do entregador: " + LatitudeDoEntregador + " \n "+ "Longitude do Entregador" + logitudeDoEntregador)
                                .setCancelable(true)
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();

                    }


                    if(statusdaentrega.equals("Pronto para a entrega")){

                        statusDoProduto = document.getString("statusDoProduto");
                        RazaodoEntregador = document.getString("RazãodoEntregador");
                        LocalizacaoEntregador = document.getString("LocalizacaoEntregador");
                        LatitudeDoEntregador = document.getString("LatitudeDoEntregador");
                        logitudeDoEntregador = document.getString("logitudeEntregador");

                        new AlertDialog.Builder(context)
                                .setTitle("Relatorio de entrega.")
                                .setMessage("Status do produto: " + statusDoProduto + " \n " + "Razão do entregador: " + RazaodoEntregador + " \n " + "Localização do entregador: " + LocalizacaoEntregador + " \n "+ "Latitude do entregador: " + LatitudeDoEntregador + " \n "+ "Longitude do Entregador" + logitudeDoEntregador)
                                .setCancelable(true)
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                        }
                    }
                }

            });
            }
            }
     });

            DocumentReference entregadorDocument =  usersDb.collection("Entregador").document(UID);

            entregadorDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    DocumentSnapshot document = task.getResult();

                    if(document.exists()){

                        entregadorName = document.get("nameCompleteUser").toString();
                        localizacaoEntregador = document.get("Localização").toString();
                        latitudeEntregador = document.get("Latitude").toString();
                        logitudeEntregador = document.get("Longitude").toString();

                        System.out.println(entregadorName);

                        entrega();

                    }
                }
            });
        }

        public void entrega(){
            DocumentReference entregaDoc =  usersDb.collection("Solicitacoes-Entregas").document(textProductID.getText().toString().replace(" ", ""));

            entregaDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    DocumentSnapshot document2 = task.getResult();

                    if(document2.exists()){


                        statusdaentrega = document2.getString("statusDoProduto");
                        LocalEntregaa = document2.getString("Local de Entrega");
                        entregadorNameFromEntrega = document2.getString("entreguePor");
                        latiInt = document2.getDouble("latitude");
                        longInt = document2.getDouble("longitude");
                        localizaçãoloja = document2.getString("Localização");

                        System.out.println("Loja: " + localizaçãoloja);

                        latitude = String.valueOf(latiInt);
                        longitude = String.valueOf(longInt);

                        System.out.println(latitude);
                        System.out.println(longitude);

                        if(statusdaentrega.equals("Em Pagamento")){

                            new AlertDialog.Builder(context)
                                    .setTitle("Em Pagamento!")
                                    .setMessage("Está em pagamento, por favor, aguarde até o pagamento ser concluido! Se o pagamento foi concluido, arraste para baixo até aparecer a bolinha branca, caso esteja como pronto para (Pronto para a Entrega), clique novamente e faça a entrega!")
                                    .setCancelable(true)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();

                        }else{

                        if(statusdaentrega.equals("Ativo")){

                        new AlertDialog.Builder(context)
                                .setTitle("Fazer a entrega?")
                                .setMessage("Você deseja realizar essa entrega?")
                                .setCancelable(true)
                                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                })
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Map<String, Object> data = new HashMap<>();
                                        data.put("entreguePor", entregadorName);
                                        data.put("uidEntregador", UID);
                                        data.put("statusDoProduto", "A caminho");
                                        data.put("RazãodoEntregador", "O Entregador está a caminho!");
                                        data.put("LocalizacaoEntregador", localizacaoEntregador);
                                        data.put("LatitudeDoEntregador", latitudeEntregador);
                                        data.put("logitudeEntregador", logitudeEntregador);


                                        DocumentReference setDB = usersDb.collection("Solicitacoes-Entregas").document(textProductID.getText().toString().replace(" ", ""));

                                        setDB.update(data);

                                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + localizaçãoloja);
                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        mapIntent.setPackage("com.google.android.apps.maps");
                                        context.startActivity(mapIntent);

                                    }
                                }).show();
                    }else{
                            if(statusdaentrega.equals("Pronto para a entrega")){

                                new AlertDialog.Builder(context)
                                        .setTitle("Fazer a entrega?")
                                        .setMessage("Você deseja realizar essa entrega até o endereço?")
                                        .setCancelable(true)
                                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Map<String, Object> data = new HashMap<>();
                                                data.put("RazãodoEntregador", "Clicou em não ao receber o produto para fazer a entrega!");
                                                data.put("LocalizacaoEntregador", localizacaoEntregador);
                                                data.put("LatitudeDoEntregador", latitudeEntregador);
                                                data.put("logitudeEntregador", logitudeEntregador);

                                                DocumentReference setDB = usersDb.collection("Solicitacoes-Entregas").document(textProductID.getText().toString().replace(" ", ""));

                                                setDB.update(data);

                                            }
                                        })
                                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Map<String, Object> data = new HashMap<>();
                                                data.put("statusDoProduto", "Em entrega");
                                                data.put("RazãodoEntregador", "Clicou em sim ao receber o produto para fazer a entrega!");
                                                data.put("LocalizacaoEntregador", localizacaoEntregador);
                                                data.put("LatitudeDoEntregador", latitudeEntregador);
                                                data.put("logitudeEntregador", logitudeEntregador);

                                                DocumentReference setDB = usersDb.collection("Solicitacoes-Entregas").document(textProductID.getText().toString().replace(" ", ""));

                                                setDB.update(data);
                                                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + LocalEntregaa);
                                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                                mapIntent.setPackage("com.google.android.apps.maps");
                                                context.startActivity(mapIntent);

                                            }
                                        }).show();

                            }else{

                                if(statusdaentrega.equals("Em entrega")){

                                    new AlertDialog.Builder(context)
                                            .setTitle("Você chegou ao destino?")
                                            .setMessage("Você chegou ao destino e entregou o pedido?")
                                            .setCancelable(false)
                                            .setPositiveButton("Cheguei, e entreguei", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Map<String, Object> data = new HashMap<>();
                                                    data.put("statusDoProduto", "Entregue");
                                                    data.put("RazãodoEntregador", "Produto entregue");
                                                    data.put("LocalizacaoEntregador", localizacaoEntregador);
                                                    data.put("LatitudeDoEntregador", latitudeEntregador);
                                                    data.put("logitudeEntregador", logitudeEntregador);

                                                    DocumentReference setDB = usersDb.collection("Solicitacoes-Entregas").document(textProductID.getText().toString().replace(" ", ""));

                                                    setDB.update(data);

                                                }
                                            }).show();

                                }else{

                            if(entregadorNameFromEntrega.equals(entregadorName)){

                                new AlertDialog.Builder(context)
                                        .setTitle("Você chegou?")
                                        .setMessage("Você chegou? Ou deseja cancelar a viagem?")
                                        .setCancelable(true)
                                        .setNegativeButton("Cancelar viagem", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Map<String, Object> data = new HashMap<>();
                                                data.put("statusDoProduto", "Ativo");
                                                data.put("uidEntregador", "");
                                                data.put("entreguePor", "");

                                                DocumentReference setDB = usersDb.collection("Solicitacoes-Entregas").document(textProductID.getText().toString().replace(" ", ""));

                                                setDB.update(data);

                                            }
                                        })
                                        .setPositiveButton("Cheguei", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Map<String, Object> data = new HashMap<>();
                                                data.put("statusDoProduto", "Em Pagamento");

                                                DocumentReference setDB = usersDb.collection("Solicitacoes-Entregas").document(textProductID.getText().toString().replace(" ", ""));

                                                setDB.update(data);

                                            }
                                        }).show();

                            }else{

                            }
                           }
                        }
                      }
                   }
                 }
               }
          });
        }
    }
}
