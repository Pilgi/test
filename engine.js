(function(root){
 'use strict';
 const D=root.LessonData;
 const same=(a,b)=>a.length===b.length&&[...new Set(a)].length===a.length&&a.every(x=>b.includes(x));
 const rule=id=>D.rules.find(r=>r.id===id);
 function apply(known,id,chosen){
   const r=rule(id);if(!r)return {ok:false,reason:'규칙을 선택하세요.'};
   const missing=r.need.filter(f=>!known.includes(f));
   if(missing.length)return {ok:false,reason:`조건 ${missing.join(' · ')}이 아직 확인되지 않았습니다. 이 규칙은 보류합니다.`,missing};
   if(known.includes(r.out))return {ok:false,reason:`${r.out}은 이미 있는 사실입니다. 다른 규칙을 확인하세요.`};
   if(!same(chosen,r.need))return {ok:false,reason:'이 규칙의 IF에 필요한 사실을 모두 골라주세요. 다른 사실은 빼고 두 조건의 근거를 확인하세요.'};
   return {ok:true,known:[...known,r.out],entry:{rule:id,used:[...r.need],out:r.out}};
 }
 function closure(input){const known=[...input],trace=[];let changed=true;while(changed){changed=false;for(const r of D.rules){if(r.need.every(f=>known.includes(f))&&!known.includes(r.out)){known.push(r.out);trace.push({rule:r.id,used:r.need,out:r.out});changed=true;}}}return {known,trace};}
 const complete=known=>D.rules.every(r=>known.includes(r.out)||r.need.some(f=>!known.includes(f)));
 function question(caseId,known,rid,fid,text){
  const c=D.cases[caseId],r=rule(rid);
  if(!c||!r)return {ok:false,reason:'보류한 규칙을 먼저 고르세요.'};
  if(!complete(known))return {ok:false,reason:'지금 사실로 적용할 수 있는 규칙이 남아 있습니다. 추가 전 추론을 먼저 끝내세요.'};
  if(!r.need.includes(fid)||known.includes(fid))return {ok:false,reason:'선택한 규칙의 IF에서 아직 없는 사실을 찾아주세요.'};
  if((text||'').trim().length<6)return {ok:false,reason:'부족한 관계를 확인할 질문을 문장으로 적어주세요.'};
  if(!c.extra.includes(fid))return {ok:false,reason:'그 조건도 부족하지만, 우선 칠게까지의 중간 경로를 열 정보를 찾아보세요. 이번 카드로 확인할 관계는 규칙의 앞선 조건에 연결됩니다.'};
  return {ok:true,fact:fid};
 }
 function assess(p){
  const expected=[{rule:'R2',used:['F2','F3'],out:'F6'},{rule:'R4',used:['F6','F5'],out:'F8'}];
  const rows=expected.map((e,i)=>same(p.rows[i]?.used||[],e.used)&&p.rows[i]?.rule===e.rule&&p.rows[i]?.out===e.out);
  return {rows,applied:rows.filter(Boolean).length*2,heldRule:p.hold==='R3',heldFact:p.missing==='F4'&&p.reason.trim().length>=6,region:p.direct==='1'&&p.inferred==='2'};
 }
 root.Inference={same,rule,apply,closure,complete,question,assess};
})(typeof window!=='undefined'?window:globalThis);
