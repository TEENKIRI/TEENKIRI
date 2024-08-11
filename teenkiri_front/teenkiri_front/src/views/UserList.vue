<template>
    <v-container>
        <v-row justify="center">
            <v-col>
                <v-card-title class="text-center text-h5">
                    회원목록
                </v-card-title>
                <v-card-text>
                    <v-data-table
                    :headers="tableHeaders"
                    :items="userList"
                    >
                    </v-data-table>
                </v-card-text>
            </v-col>
        </v-row>
    </v-container>
</template>
<script>
import axios from 'axios';
export default{
    data(){
        return{
            tableHeaders:[
                {title:'ID', key:'id', align:'start'},
                {title:'이름', key:'name', align:'start'},
                {title:'EMAIL', key:'email',align:'start'},
                {title:'주소', key:'address',align:'start'},
                {title:'전화번호', key:'phone',align:'start'},
                {title:'닉네임', key:'nickname',align:'start'},
                {title:'신고횟수', key:'reportCount',align:'start'},
                {title:'생년월일', key:'birth', align:'start'},
                {title:'사용자구분', key:'role',align:'start'},
            ],
            userList:[]
        }
    },
    async created(){
       try{
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/user/list`);
        this.userList = response.data.result.content;
        }catch(e){
            console.log(e);
        }
    }
}
</script>

<style>
.text-center{
    text-align:center;
}
</style>