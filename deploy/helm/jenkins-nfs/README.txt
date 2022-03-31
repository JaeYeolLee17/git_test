** Jenkins values 수정사항

1. persistence volume
   nfs pv, pvc 만들어서 existingClaim 에 pvc 설정.

2. service account
   create: false
   name 에 sa 만들어서 설정.

3. agent memory limit 상향.
   기본 설정으로는 memory 부족하여 gradle build 시 gradle deamon 강제 중단됨.

4. agent workspace volume
   workspaceVolume 에 nfs 로 설정


-----------------------
** 위 설정 전 먼저 준비할 사항.

1. nfs 공유 dir 설정.
   infra > nfs 참조.

-----------------------
** 최초 Jenkins 구동 후 plugin 모두 update 필수!!
오류 없을때 까지
