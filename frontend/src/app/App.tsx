import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
function App() {
  return (
    <>
      <p className="m-4 mt-2 text-center text-4xl font-bold">Asset Manager</p>
      <p className="m-4 mt-0 text-center text-lg text-gray-700">
        Lorem ipsum dolor sit amet consectetur adipisicing elit. Porro est,
        maiores nam enim qui tenetur corrupti sunt eveniet nihil blanditiis
        expedita, minus pariatur accusamus eaque quas! Pariatur consectetur
        accusantium est.
      </p>
      <Layout>
        <LayoutRow>
          <LayoutColumn mdSpan={6} lgSpan={4}>
            <p className="mt-2 text-center text-lg md:text-left">
              Lorem ipsum dolor sit amet consectetur adipisicing elit. Porro
              est, maiores nam enim qui tenetur corrupti sunt eveniet nihil
              blanditiis expedita, minus pariatur accusamus eaque quas! Pariatur
              consectetur accusantium est.
            </p>
          </LayoutColumn>
          <LayoutColumn mdSpan={6} lgSpan={4}>
            <p className="mt-2 text-center text-lg">
              Lorem ipsum dolor sit amet consectetur adipisicing elit. Porro
              est, maiores nam enim qui tenetur corrupti sunt eveniet nihil
              blanditiis expedita, minus pariatur accusamus eaque quas! Pariatur
              consectetur accusantium est.
            </p>
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </>
  );
}

export default App;
