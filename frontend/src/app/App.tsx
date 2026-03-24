import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
function App() {
  const arr = Array.from({ length: 12 }, (_, i) => i);
  const boxClass =
    'mb-8 flex h-32 items-center justify-center rounded-lg bg-gray-200 text-2xl font-bold';
  return (
    <>
      <Layout className="bg-yellow-300">
        <LayoutRow>
          <LayoutColumn>
            <h1 className="my-8 text-center text-5xl font-bold">
              Asset manager
            </h1>
          </LayoutColumn>
        </LayoutRow>
      </Layout>
      <Layout>
        <LayoutRow className="mt-8">
          {arr.map((i) => (
            <LayoutColumn key={i} span={1}>
              <div className={boxClass}>{i + 1}</div>
            </LayoutColumn>
          ))}
        </LayoutRow>
        <LayoutRow>
          <LayoutColumn span={12}>
            <hr className="mt-8 mb-16" />

            <div className={boxClass}>span=12</div>
          </LayoutColumn>
          <LayoutColumn span={6}>
            <div className={boxClass}>span=6</div>
          </LayoutColumn>
          <LayoutColumn span={6}>
            <div className={boxClass}>span=6</div>
          </LayoutColumn>
          <LayoutColumn span={4}>
            <div className={boxClass}>span=4</div>
          </LayoutColumn>
          <LayoutColumn span={4}>
            <div className={boxClass}>span=4</div>
          </LayoutColumn>
          <LayoutColumn span={4}>
            <div className={boxClass}>span=4</div>
          </LayoutColumn>
          <LayoutColumn span={3}>
            <div className={boxClass}>span=3</div>
          </LayoutColumn>
          <LayoutColumn span={3}>
            <div className={boxClass}>span=3</div>
          </LayoutColumn>
          <LayoutColumn span={3}>
            <div className={boxClass}>span=3</div>
          </LayoutColumn>
          <LayoutColumn span={3}>
            <div className={boxClass}>span=3</div>
          </LayoutColumn>
        </LayoutRow>
        <LayoutRow>
          <LayoutColumn>
            <hr className="mt-8 mb-16" />
          </LayoutColumn>
          <LayoutColumn span={4}>
            <div className={boxClass}>span=4</div>
          </LayoutColumn>
          <LayoutColumn offset={3} span={5}>
            <div className={boxClass}>span=5 offset=3</div>
          </LayoutColumn>
          <LayoutColumn span={2}>
            <div className={boxClass}>span=2</div>
          </LayoutColumn>
          <LayoutColumn span={2}>
            <div className={boxClass}>span=2</div>
          </LayoutColumn>
          <LayoutColumn offset={2} span={6}>
            <div className={boxClass}>span=6 offset=2</div>
          </LayoutColumn>
        </LayoutRow>
        <LayoutRow>
          <LayoutColumn>
            <hr className="mt-8 mb-16" />
          </LayoutColumn>
          <LayoutColumn smSpan={12} mdSpan={6} lgSpan={4} xlSpan={3}>
            <div className={boxClass}>Responsive column</div>
          </LayoutColumn>
          <LayoutColumn smSpan={12} mdSpan={6} lgSpan={4} xlSpan={3}>
            <div className={boxClass}>Responsive column</div>
          </LayoutColumn>
          <LayoutColumn smSpan={12} mdSpan={6} lgSpan={4} xlSpan={3}>
            <div className={boxClass}>Responsive column</div>
          </LayoutColumn>
          <LayoutColumn smSpan={12} mdSpan={6} lgSpan={4} xlSpan={3}>
            <div className={boxClass}>Responsive column</div>
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </>
  );
}

export default App;
