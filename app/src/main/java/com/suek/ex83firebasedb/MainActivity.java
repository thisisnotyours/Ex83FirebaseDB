package com.suek.ex83firebasedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText et;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et= findViewById(R.id.et);
        tv= findViewById(R.id.tv);
    }

    public void clickBtn(View view) {
        //EditText 에 있는 글씨 얻어오기
        String text= et.getText().toString();


        //Firebase 실시간 DB 에 저장.. ()

        //1. Firebase 실시간 DB 관리객체 얻어오기
        FirebaseDatabase db= FirebaseDatabase.getInstance();

        //2. 데이터를 저장시킬 노드 참조객체 가져오기
        DatabaseReference rootRef= db.getReference();   //초상위 노드

        //3. 해당노드에 값 설정
        //3.1) 값 갱신  -----> 값이 바꿔치기만 됨.
        /*rootRef.setValue(text);

        //4. DB의 값 변경을 실시간 읽어오기
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //값이 변경되었을때 실행되는 메소드

                //별도의 값 읽기 버튼이 필요없음. 실시간 읽어옴.

                //이 파라미터로 전달된 DataSnapshot 객체가 데이터를 가져옴.
                String data= dataSnapshot.getValue(String.class);    //String 자료형으로 리턴값 변환해줌
                tv.setText(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        //3.2) 위 3.1방법은 값이 바꿔치기 됨. 즉, 갱신
        //     값을 누적하여 추가하고 싶다면..
        /*DatabaseReference ref1= rootRef.push();  //push 하면 자식노드가 새로 생기고 그 참조객체 리턴
        ref1.setValue(text);  //text 값은 EditText 에서 받아온것

        //값 읽기
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //최상위에 바로 값이 있는 것이 아님..  자식노드들이 여러개 이므로..
                //반복문을 이용해서 값들을 읽어오기
                StringBuffer buffer= new StringBuffer();   //원래 여기서 list 방식으로..
                for( DataSnapshot snap : dataSnapshot.getChildren() ){   //큰 스냅샷안에 각각의 스냅샷들이 있는 것..
                    String data= snap.getValue(String.class);
                    buffer.append(data+"\n");
                }

                tv.setText(buffer.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        //3.3) 자식노드에 '키'값 지정하여 만들기
        /*DatabaseReference dataRef= rootRef.child("data");
        //dataRef.setValue("aaa");
        //data 노드의 자식으로 여러개의 값을 저장
        dataRef.push().setValue(text);

        //data 노드의 변경만 읽어오면 되므로..
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String s="";
                for( DataSnapshot snap : dataSnapshot.getChildren()){
                    String data= snap.getValue(String.class);
                    s += data+"\n";
                }

                tv.setText(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/



        //3.4) 하나의 노드에 Value 가 여러개..
        /*DatabaseReference memberRef= rootRef.child("member");
        DatabaseReference itemRef= memberRef.push();   //'member' 라는 이름의 노드의 아래에 임의의 식별자를 가진 자식노드가 생김
        itemRef.child("name").setValue(text);
        itemRef.child("age").setValue(20);*/


        //3.5) 나만의 클래스[VO: Value Object]객체를 만들어서 한번에 멤버값들 저장하기
        String name= et.getText().toString();
        int age= 20;
        String address= "seoul";

        //저장할 값들을 하나의 객체로 생성하기
        Person person= new Person(name, age, address);

        //서버에/Firebase 에 'Person'라는 이름의 자식노드를 참조하는 객체 생성 또는 참조
        DatabaseReference personRef= rootRef.child("persons");   //persons 테이블같은..
        personRef.push().setValue(person);


        //'persons'노드의 벨류가 변경되는 것을 듣는 리스너 추가!
        personRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //변화되는 순간에 캡쳐
                //Person 이 여러개 이므로 반복문
                for(DataSnapshot snapshot : dataSnapshot.getChildren() ){   //자식들 하나씩 캡쳐
                    Person p= snapshot.getValue(Person.class);
                    tv.append(p.name + ", " + p.age + ", " + p.address + "\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
