/* Reconstructed learning texts, not quotations or purported news reports. */
window.LessonData = {
  facts: {
    F1:'미세조류가 광합성함', F2:'미세조류가 빛에너지를 양분에 저장함',
    F3:'칠게가 미세조류를 먹음', F4:'저어새가 칠게를 먹음', F5:'검은머리물떼새가 칠게를 먹음',
    F6:'미세조류의 에너지가 칠게로 전달될 수 있음',
    F7:'미세조류의 에너지가 칠게를 거쳐 저어새로 전달될 수 있음',
    F8:'미세조류의 에너지가 칠게를 거쳐 검은머리물떼새로 전달될 수 있음'
  },
  rules:[{id:'R1',need:['F1'],out:'F2'},{id:'R2',need:['F2','F3'],out:'F6'},
    {id:'R3',need:['F6','F4'],out:'F7'},{id:'R4',need:['F6','F5'],out:'F8'}],
  cases:{'가':{initial:['F1','F3','F5'],extra:['F4']},'나':{initial:['F1','F4','F5'],extra:['F3']},'다':{initial:['F1','F3'],extra:['F4','F5']}},
  readings:[
    {id:'r1',tag:'글 1 · 에너지의 출발점',title:'작은 생물이 빛을 붙잡는 방법',scope:'대부도 연구 + 광합성의 일반 원리',sources:['B5','B1'],
      paragraphs:[
        '갯벌이 드러나면 진흙과 모래의 표면이 눈에 들어온다. 그곳에는 맨눈으로 하나씩 보기 어려운 미세조류도 산다. 대부도 갯벌에서도 이 작은 생물들의 광합성과 일차생산이 연구됐다.',
        '이 수업에서 다루는 미세조류는 광합성을 한다. 광합성은 빛에너지를 이용해 양분을 만들고 그 안에 에너지를 저장하는 과정이다. 따라서 이 미세조류가 광합성한다는 정보를 확인하면, 빛에너지를 양분에 저장한다는 사실을 함께 표현할 수 있다.',
        '규칙을 만들 때는 긴 글을 그대로 옮길 필요가 없다. 조건이 되는 사실과, 그 조건에서 얻을 사실을 각각 하나의 문장으로 골라보자.'
      ], evidence:[{id:'a',text:'이 수업에서 다루는 미세조류는 광합성을 한다.'},{id:'b',text:'광합성은 빛에너지를 양분에 저장하는 과정이다.'},{id:'c',text:'미세조류는 맨눈으로 하나씩 보기 어렵다.'}],
      correctEvidence:['a','b'],conditions:['F1','F3','F4'],conclusions:['F2','F6','F7'],rule:'R1',prompt:'광합성한다는 조건에서 어떤 사실을 얻을 수 있나요?'},
    {id:'r2',tag:'글 2 · 두 사실을 함께 읽기',title:'미세조류와 칠게를 잇는 관계',scope:'일본 다마강 갯벌의 종별 연구 + 수업용 관계 해석',sources:['B2','B1'],
      paragraphs:[
        '칠게는 갯벌 표면을 다니며 먹이를 얻는다. 일본 다마강 갯벌의 연구에서는 칠게의 섭식이 표면 미세조류에 미치는 영향을 조사했다. 이는 칠게와 미세조류의 먹이 관계를 이해하는 자료다. 대부도에서 같은 날 관찰한 기록은 아니다.',
        '앞의 글에서 미세조류가 양분에 에너지를 저장한다는 사실을 얻었다. 여기에 칠게가 그 미세조류를 먹는다는 관계가 함께 확인되면, 저장된 에너지가 먹이를 통해 칠게로 전달될 수 있는 경로를 연결할 수 있다.',
        '에너지를 저장한다는 사실만으로 먹는 생물을 정할 수는 없다. 반대로 먹이 관계만 적어서는 이번 규칙이 요구하는 두 조건을 모두 나타내지 못한다. 이 수업의 모형에서는 두 사실을 그리고(AND)로 묶어 새 경로를 표현한다.'
      ],evidence:[{id:'a',text:'미세조류가 양분에 에너지를 저장한다.'},{id:'b',text:'칠게가 그 미세조류를 먹는다.'},{id:'c',text:'칠게의 에너지 전달 경로를 연결하려면 위 두 사실을 함께 확인한다.'},{id:'d',text:'이 먹이 관계 연구는 일본 다마강 갯벌에서 이루어졌다.'}],
      correctEvidence:['a','b','c'],conditions:['F2','F3','F4','F6'],conclusions:['F2','F6','F7'],rule:'R2',prompt:'칠게까지의 경로를 설명하려면 어떤 두 조건이 모두 필요할까요?'},
    {id:'r3',tag:'글 3 · 앞의 결론을 다시 쓰기',title:'칠게를 거쳐 저어새까지',scope:'인천 남동유수지 저어새 먹이 연구 + 연결 추론',sources:['B3','B1'],
      paragraphs:[
        '저어새의 먹이를 조사한 연구에서는 토사물에 남은 먹이 흔적을 분석했다. 인천 남동유수지의 저어새 시료를 현미경으로 분석한 결과에는 칠게가 포함돼 있었다. 이 글에서는 저어새가 칠게를 먹는 관계를 사용한다.',
        '그 관계만으로 미세조류에서 저어새까지의 전체 경로가 완성되는 것은 아니다. 미세조류의 에너지가 칠게로 전달될 수 있다는 앞의 결론도 필요하다. 그 경로와 저어새의 섭식 관계를 함께 사용하면, 에너지가 칠게를 거쳐 저어새로 이어질 수 있는 간접 경로를 얻는다.',
        '이렇게 앞에서 얻은 사실이 다음 규칙의 조건이 된다. 저어새가 미세조류를 직접 먹는다는 뜻으로 줄이지 말자. 이 결론은 연구 관계를 연결한 가능 경로이며 특정 날짜의 실제 섭식 기록은 아니다.'
      ],evidence:[{id:'a',text:'미세조류의 에너지가 칠게로 전달될 수 있다.'},{id:'b',text:'저어새가 칠게를 먹는다.'},{id:'c',text:'두 사실을 연결하면 미세조류의 에너지가 칠게를 거쳐 저어새로 이어질 수 있다.'},{id:'d',text:'저어새가 미세조류를 직접 먹는다.'}],
      correctEvidence:['a','b','c'],conditions:['F2','F6','F4','F5'],conclusions:['F6','F7','F2'],rule:'R3',prompt:'앞의 결론 중 무엇을 다시 조건으로 사용할 수 있나요?'},
    {id:'r4',tag:'글 4 · 나의 규칙 만들기',title:'또 다른 새로 경로를 넓혀볼까?',scope:'유부도 검은머리물떼새 먹이 연구 + 규칙 표현 과제',sources:['B4'],
      paragraphs:[
        '검은머리물떼새는 이름만으로 먹이 전체를 알 수 있는 새가 아니다. 유부도 번식기의 먹이 연구에서는 DNA 분석을 이용해 다양한 먹이생물을 조사했다. 그 결과에는 칠게와의 먹이 관계가 포함됐다.',
        '이제 칠게까지 얻은 에너지 경로를 이 새의 섭식 관계와 연결해보자. 저어새에 사용한 규칙을 참고하되, 어떤 조건을 유지하고 어떤 조건을 바꿔야 하는지 글에서 근거를 찾아 설명한다.',
        '두 조건은 사실 이름표로 골라도 된다. 새 결론은 칠게를 거친다는 뜻이 드러나도록 자신의 문장으로 써보자. 답을 확인한 뒤에도 처음 쓴 문장은 남겨 비교한다.'
      ]}
  ],
  sources:[
    ['L1','람사르협약 · 대부도 갯벌','https://www.ramsar.org/news/republic-korea-names-daebudo-tidal-flat-ramsar-site'],
    ['L2','EAAFP · 대부도 지역 생물 기록','https://www.eaaflyway.net/wp-content/uploads/2020/04/EAAF148_ROK_Daebudo-Tidal-Flat_Final.pdf'],
    ['L3','경기도메모리 · 남사리 갯벌 과거 조사','https://memory.library.kr/files/original/ffee111b2813abe5b57a98e033a73f60.pdf'],
    ['L4','안산시 · 대부도 람사르습지','https://ansan.go.kr/tourinfo/common/cntnts/selectContents.do?cntnts_id=t0002066'],
    ['B1','NOAA · 먹이 관계와 에너지','https://www.noaa.gov/education/resource-collections/marine-life/aquatic-food-webs'],
    ['B2','Tanaka 외(2017) · 다마강 칠게·미세저서조류 연구','https://www.jstage.jst.go.jp/article/pbr/12/1/12_B120105/_article'],
    ['B3','김현정 외(2018) · 저어새 먹이생물 분석','https://www.ebr.or.kr/journal/article.php?code=62525'],
    ['B4','Joo 외(2025) · 유부도 검은머리물떼새 먹이 분석','https://www.accesson.kr/jecoenv/assets/pdf/58235/journal-49-4-268.pdf'],
    ['B5','Kwon 외(2018) · 대부도 미세저서조류 일차생산','https://pubmed.ncbi.nlm.nih.gov/29803025/'],
    ['G1','NOAA · 조간대 설명','https://oceanservice.noaa.gov/facts/intertidal-zone.html'],
    ['AI','Poole·Mackworth · 전방향 추론','https://www.cs.ubc.ca/~poole/aibook/3e/html/ArtInt3e.Ch5.S3.html'],
    ['APP','NASA · SHINE 활용 사례','https://spinoff.nasa.gov/Expertise-Flows-from-Computer-Cognition'],
    ['ML','IBM · AI·기계학습·딥러닝','https://www.ibm.com/think/topics/ai-vs-machine-learning-vs-deep-learning-vs-neural-networks'],
    ['NS','IBM Research · 신경기호 AI','https://research.ibm.com/topics/neuro-symbolic-ai']
  ]
};
